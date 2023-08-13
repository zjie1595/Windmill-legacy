package com.zj.windmill

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.blankj.utilcode.util.AppUtils
import com.zj.windmill.data.local.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideOkHttpClient(@ApplicationContext context: Context): OkHttpClient {
        val builder = OkHttpClient.Builder()
        val cacheDir = File(context.filesDir, "http_cache")
        if (!cacheDir.exists()) {
            cacheDir.mkdir()
        }
        val cacheSize = 100L * 1024 * 1024
        val cache = Cache(cacheDir, cacheSize)
        val cacheControl = CacheControl.Builder()
            .maxAge(8, TimeUnit.HOURS)
            .build()
        if (AppUtils.isAppDebug()) {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC
            builder.addInterceptor(httpLoggingInterceptor)
        }
        // 设置缓存
        builder.cache(cache)
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .cacheControl(cacheControl)
                    .build()
                chain.proceed(request)
            }
        return builder.build()
    }

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    @Singleton
    @Provides
    fun provideDatastore(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.dataStore
    }

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "db_windmill").build()
    }

    @Singleton
    @Provides
    fun provideApplicationScope(): CoroutineScope {
        return CoroutineScope(Dispatchers.IO + Job())
    }
}