package com.zj.windmill

import android.app.Application
import android.widget.TextView
import coil.ImageLoader
import coil.ImageLoaderFactory
import com.blankj.utilcode.util.AppUtils
import com.drake.brv.PageRefreshLayout
import com.drake.brv.utils.BRV
import com.drake.statelayout.StateConfig
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.shuyu.gsyvideoplayer.cache.CacheFactory
import com.shuyu.gsyvideoplayer.player.PlayerFactory
import com.shuyu.gsyvideoplayer.utils.Debuger
import com.shuyu.gsyvideoplayer.utils.GSYVideoType
import dagger.hilt.android.HiltAndroidApp
import tv.danmaku.ijk.media.exo2.Exo2PlayerManager
import tv.danmaku.ijk.media.exo2.ExoPlayerCacheManager

@HiltAndroidApp
class MyApp : Application(), ImageLoaderFactory {

    override fun onCreate() {
        super.onCreate()
        initBrv()
        initGsyVideoPlayer()
    }

    override fun newImageLoader(): ImageLoader {
        val builder = ImageLoader.Builder(this)
        if (AppUtils.isAppDebug()) {
            builder.logger(CoilLogger())
        }
        return builder.build()
    }

    private fun initBrv() {
        BRV.modelId = BR.m
        PageRefreshLayout.refreshEnableWhenError = false
        StateConfig.apply {
            emptyLayout = R.layout.layout_empty
            errorLayout = R.layout.layout_error
            loadingLayout = R.layout.layout_loading

            setRetryIds(R.id.msg, R.id.iv)

            onError {
                if (tag is String) {
                    val errorMessage = tag as String
                    val msg = findViewById<TextView>(R.id.msg)
                    msg.text = errorMessage
                }
            }
        }
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, _ ->
            ClassicsHeader(context)
        }
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, _ ->
            ClassicsFooter(context)
        }
        PageRefreshLayout.startIndex = 0
    }

    private fun initGsyVideoPlayer() {
        PlayerFactory.setPlayManager(Exo2PlayerManager::class.java)
        CacheFactory.setCacheManager(ExoPlayerCacheManager::class.java)
        GSYVideoType.setRenderType(GSYVideoType.SUFRACE)
        if (AppUtils.isAppDebug()) {
            Debuger.enable()
        }
    }
}