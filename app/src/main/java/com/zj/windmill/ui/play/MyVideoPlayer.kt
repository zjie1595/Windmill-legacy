package com.zj.windmill.ui.play

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.utils.OrientationUtils
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer
import com.zj.windmill.R

class MyVideoPlayer : StandardGSYVideoPlayer, DefaultLifecycleObserver {
    @Suppress("unused")
    constructor(context: Context?, fullFlag: Boolean?) : super(context, fullFlag)
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    private var orientationUtils: OrientationUtils? = null

    private val videoCallback = object : VideoCallback() {
        override fun onPrepared(url: String?, vararg objects: Any?) {
            super.onPrepared(url, *objects)
            orientationUtils?.isEnable = true
        }

        override fun onQuitFullscreen(url: String?, vararg objects: Any?) {
            super.onQuitFullscreen(url, *objects)
            orientationUtils?.backToProtVideo()
        }
    }

    init {
        if (context is Activity) {
            orientationUtils = OrientationUtils(context as Activity, this).apply {
                isEnable = false
            }
        }
        if (context is LifecycleOwner) {
            val lifecycleOwner = context as LifecycleOwner
            lifecycleOwner.lifecycle.addObserver(this)
        }
        setVideoAllCallBack(videoCallback)

        fullscreenButton.setOnClickListener {
            orientationUtils?.resolveByClick()
            startWindowFullscreen(context, false, true)
        }
        isNeedOrientationUtils = true
        isShowFullAnimation = false
        isNeedLockFull = true
        setLockClickListener { _, lock ->
            orientationUtils?.isEnable = !lock
        }
    }

    override fun init(context: Context) {
        super.init(context)
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        GSYVideoManager.onPause()
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        GSYVideoManager.onResume()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        release()
    }

    override fun getLayoutId(): Int {
        return R.layout.my_video_player
    }

    override fun getEnlargeImageRes(): Int {
        return R.drawable.fullscreen
    }

    override fun getShrinkImageRes(): Int {
        return R.drawable.fullscreen
    }

    override fun lockTouchLogic() {
        if (mLockCurScreen) {
            mLockScreen.setImageResource(R.drawable.lock_open)
            mLockCurScreen = false
        } else {
            mLockScreen.setImageResource(R.drawable.locked)
            mLockCurScreen = true
            hideAllWidget()
        }
    }

}