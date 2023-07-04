package com.zj.windmill

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import com.blankj.utilcode.util.AppUtils
import com.drake.brv.PageRefreshLayout
import com.drake.brv.utils.BRV
import com.drake.statelayout.StateConfig
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.MaterialHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApp : Application(), ImageLoaderFactory {

    override fun onCreate() {
        super.onCreate()
        initLogger()
        initBrv()
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

    private fun initBrv() {
        BRV.modelId = BR.m
        PageRefreshLayout.refreshEnableWhenError = false
        StateConfig.apply {
            emptyLayout = R.layout.layout_empty
            errorLayout = R.layout.layout_error
            loadingLayout = R.layout.layout_loading

            setRetryIds(R.id.msg, R.id.iv)
        }
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, _ ->
            MaterialHeader(context)
        }
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, _ ->
            ClassicsFooter(context)
        }
    }
}