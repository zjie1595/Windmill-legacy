package com.zj.windmill

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.blankj.utilcode.util.AppUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        if (AppUtils.isAppDebug()) {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC
            builder.addInterceptor(httpLoggingInterceptor)
        }
        return builder.build()
    }

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    @Singleton
    @Provides
    fun provideDatastore(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.dataStore
    }
}