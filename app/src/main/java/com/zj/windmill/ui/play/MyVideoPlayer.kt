package com.zj.windmill.ui.play

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.utils.OrientationUtils
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer
import com.zj.windmill.R
import com.zj.windmill.data.local.AppDatabase
import com.zj.windmill.data.remote.DetailPageParser
import com.zj.windmill.data.remote.VideoUrlParser
import com.zj.windmill.model.Video
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class MyVideoPlayer : StandardGSYVideoPlayer, DefaultLifecycleObserver, VideoPlayer {
    @Suppress("unused")
    constructor(context: Context?, fullFlag: Boolean?) : super(context, fullFlag)
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    private var orientationUtils: OrientationUtils? = null

    private lateinit var errorMessageView: TextView
    private lateinit var loadingMessageView: TextView
    private lateinit var lockScreenLeft: ImageView
    private lateinit var timeTextView: TextView

    @Inject
    lateinit var detailPageParser: DetailPageParser

    @Inject
    lateinit var database: AppDatabase

    private val coroutineScope = CoroutineScope(Dispatchers.Main + Job())

    val playController by lazy {
        PlayController(
            VideoUrlParser(context as AppCompatActivity),
            detailPageParser,
            database,
            this
        )
    }

    private val lifecycleScope by lazy {
        (context as AppCompatActivity).lifecycleScope
    }

    fun setup(video: Video?) {
        lifecycleScope.launch {
            playController.setupVideo(video)
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        coroutineScope.launch {
            playController.exitPage()
        }
    }

    private val videoCallback = object : VideoCallback() {
        override fun onPrepared(url: String?, vararg objects: Any?) {
            super.onPrepared(url, *objects)
            orientationUtils?.isEnable = true
        }

        override fun onQuitFullscreen(url: String?, vararg objects: Any?) {
            super.onQuitFullscreen(url, *objects)
            orientationUtils?.backToProtVideo()
        }

        override fun onComplete(url: String?, vararg objects: Any?) {
            super.onComplete(url, *objects)
            // 尝试播放下一集？？？
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
        isShowDragProgressTextOnSeekBar = true
        setLockClickListener { _, lock ->
            orientationUtils?.isEnable = !lock
        }
        mDismissControlTime = 3500
    }

    override fun init(context: Context) {
        super.init(context)
        errorMessageView = findViewById(R.id.error_message)
        loadingMessageView = findViewById(R.id.loading)
        lockScreenLeft = findViewById<ImageView?>(R.id.lock_screen_left).apply {
            isVisible = false
            setOnClickListener {
                if (mCurrentState == CURRENT_STATE_AUTO_COMPLETE ||
                    mCurrentState == CURRENT_STATE_ERROR
                ) {
                    return@setOnClickListener
                }
                lockTouchLogic()
                if (mLockClickListener != null) {
                    mLockClickListener.onClick(this, mLockCurScreen)
                }
            }
        }
        timeTextView = findViewById(R.id.time)
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
            lockScreenLeft.setImageResource(R.drawable.lock_open)
            mLockCurScreen = false
        } else {
            mLockScreen.setImageResource(R.drawable.locked)
            lockScreenLeft.setImageResource(R.drawable.locked)
            mLockCurScreen = true
            hideAllWidget()
        }
    }

    override fun onClickUiToggle(e: MotionEvent?) {
        if (mIfCurrentIsFullscreen && mLockCurScreen && mNeedLockFull) {
            setViewShowState(mLockScreen, VISIBLE)
            setViewShowState(lockScreenLeft, VISIBLE)
            return
        }

        if (mIfCurrentIsFullscreen && !mSurfaceErrorPlay && (mCurrentState == CURRENT_STATE_ERROR)) {
            if (mBottomContainer != null) {
                if (mBottomContainer.visibility == VISIBLE) {
                    changeUiToPlayingClear()
                } else {
                    changeUiToPlayingShow()
                }
            }
        } else if (mCurrentState == CURRENT_STATE_PREPAREING) {
            if (mBottomContainer != null) {
                if (mBottomContainer.visibility == VISIBLE) {
                    changeUiToPrepareingClear()
                } else {
                    changeUiToPreparingShow()
                }
            }
        } else if (mCurrentState == CURRENT_STATE_PLAYING) {
            if (mBottomContainer != null) {
                if (mBottomContainer.visibility == VISIBLE) {
                    changeUiToPlayingClear()
                } else {
                    changeUiToPlayingShow()
                }
            }
        } else if (mCurrentState == CURRENT_STATE_PAUSE) {
            if (mBottomContainer != null) {
                if (mBottomContainer.visibility == VISIBLE) {
                    changeUiToPauseClear()
                } else {
                    changeUiToPauseShow()
                }
            }
        } else if (mCurrentState == CURRENT_STATE_AUTO_COMPLETE) {
            if (mBottomContainer != null) {
                if (mBottomContainer.visibility == VISIBLE) {
                    changeUiToCompleteClear()
                } else {
                    changeUiToCompleteShow()
                }
            }
        } else if (mCurrentState == CURRENT_STATE_PLAYING_BUFFERING_START) {
            if (mBottomContainer != null) {
                if (mBottomContainer.visibility == VISIBLE) {
                    changeUiToPlayingBufferingClear()
                } else {
                    changeUiToPlayingBufferingShow()
                }
            }
        }
    }

    override fun changeUiToNormal() {
        super.changeUiToNormal()
        setViewShowState(errorMessageView, INVISIBLE)
    }

    override fun changeUiToPreparingShow() {
        super.changeUiToPreparingShow()
        setViewShowState(errorMessageView, INVISIBLE)
        setViewShowState(mStartButton, VISIBLE)
    }

    override fun changeUiToClear() {
        super.changeUiToClear()
        setViewShowState(errorMessageView, INVISIBLE)
    }

    override fun changeUiToPauseShow() {
        super.changeUiToPauseShow()
        setViewShowState(errorMessageView, INVISIBLE)
    }

    override fun changeUiToCompleteShow() {
        super.changeUiToCompleteShow()
        setViewShowState(errorMessageView, INVISIBLE)
    }

    override fun changeUiToPauseClear() {
        super.changeUiToPauseClear()
        setViewShowState(errorMessageView, INVISIBLE)
    }

    override fun changeUiToCompleteClear() {
        super.changeUiToCompleteClear()
        setViewShowState(errorMessageView, INVISIBLE)
    }

    override fun changeUiToPlayingBufferingClear() {
        super.changeUiToPlayingBufferingClear()
        setViewShowState(errorMessageView, INVISIBLE)
    }

    override fun changeUiToPlayingBufferingShow() {
        super.changeUiToPlayingBufferingShow()
        setViewShowState(errorMessageView, INVISIBLE)
        setViewShowState(mStartButton, VISIBLE)
    }

    override fun changeUiToPlayingClear() {
        super.changeUiToPlayingClear()
        setViewShowState(errorMessageView, INVISIBLE)
    }

    override fun changeUiToPlayingShow() {
        super.changeUiToPlayingShow()
        setViewShowState(errorMessageView, INVISIBLE)
    }

    override fun changeUiToPrepareingClear() {
        super.changeUiToPrepareingClear()
        setViewShowState(errorMessageView, INVISIBLE)
    }

    override fun changeUiToError() {
        super.changeUiToError()
        setViewShowState(errorMessageView, VISIBLE)
        setViewShowState(mStartButton, INVISIBLE)
    }

    override fun updateStartImage() {
        val imageView = mStartButton as ImageView
        val resId = if (mCurrentState == CURRENT_STATE_PLAYING) {
            R.drawable.pause
        } else {
            R.drawable.play
        }
        imageView.setImageResource(resId)
    }

    fun showLoading(loadingText: String = "加载中...") {
        setStateAndUi(CURRENT_STATE_PREPAREING)
        loadingMessageView.text = loadingText
    }

    override fun onAutoCompletion() {
        super.onAutoCompletion()
        if (mLockCurScreen) {
            lockScreenLeft.isVisible = false
        }
    }

    override fun onError(what: Int, extra: Int) {
        super.onError(what, extra)
        if (mLockCurScreen) {
            lockScreenLeft.isVisible = false
        }
    }

    fun showError(errorMessage: String = "发生未知错误") {
        setStateAndUi(CURRENT_STATE_ERROR)
        errorMessageView.text = errorMessage
    }

    override fun hideAllWidget() {
        super.hideAllWidget()
        setViewShowState(lockScreenLeft, GONE)
    }

    override fun contentPosition(): Long {
        return currentPositionWhenPlaying
    }

    override fun contentDuration(): Long {
        return duration
    }

    override fun playVideo(videoUrl: String, title: String?, contentPosition: Long?) {
        setUp(videoUrl, true, title)
        if (contentPosition != null) {
            seekOnStart = contentPosition
        }
        startPlayLogic()
    }

    override fun showParseError(throwable: Throwable) {
        showError("视频解析失败")
    }

    override fun showParsing() {
        showLoading("视频解析中...")
    }

    override fun showUnknownError() {
        showError("未知异常")
    }

    @SuppressLint("SetTextI18n")
    override fun setStateAndUi(state: Int) {
        super.setStateAndUi(state)
        if (currentState == CURRENT_STATE_AUTO_COMPLETE) {
            updateTimeTextView()
        }
    }

    @SuppressLint("SetTextI18n")
    override fun showDragProgressTextOnSeekBar(fromUser: Boolean, progress: Int) {
        super.showDragProgressTextOnSeekBar(fromUser, progress)
        if (fromUser && isShowDragProgressTextOnSeekBar) {
            updateTimeTextView()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateTimeTextView() {
        timeTextView.text = "${mCurrentTimeTextView.text}/${mTotalTimeTextView.text}"
    }

    override fun setProgressAndTime(
        progress: Long,
        secProgress: Long,
        currentTime: Long,
        totalTime: Long,
        forceChange: Boolean
    ) {
        super.setProgressAndTime(progress, secProgress, currentTime, totalTime, forceChange)
        if (currentTime > 0) {
            updateTimeTextView()
        }
    }

    override fun resetProgressAndTime() {
        super.resetProgressAndTime()
        if (mProgressBar == null || mTotalTimeTextView == null || mCurrentTimeTextView == null) {
            return
        }
        updateTimeTextView()
    }

    override fun loopSetProgressAndTime() {
        super.loopSetProgressAndTime()
        if (mProgressBar == null || mTotalTimeTextView == null || mCurrentTimeTextView == null) {
            return
        }
        updateTimeTextView()
    }
}