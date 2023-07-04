package com.zj.windmill

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import com.blankj.utilcode.util.AppUtils
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApp : Application(), ImageLoaderFactory {

    override fun onCreate() {
        super.onCreate()
        initLogger()
    }

    override fun newImageLoader(): ImageLoader {
        val builder = ImageLoader.Builder(this)
        if (AppUtils.isAppDebug()) {
            builder.logger(CoilLogger())
        }
        return builder.build()
    }

    private fun initLogger() {
        val prettyFormatStrategy = PrettyFormatStrategy.newBuilder()
            .showThreadInfo(false)
            .methodCount(0)
            .methodOffset(7)
            .tag("Windmill")
            .build()
        val logAdapter = object : AndroidLogAdapter(prettyFormatStrategy) {
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                return AppUtils.isAppDebug()
            }
        }
        Logger.addLogAdapter(logAdapter)
    }
}