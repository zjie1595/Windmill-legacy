package com.zj.windmill.ui.play

import android.os.Bundle
import com.shuyu.gsyvideoplayer.GSYBaseActivityDetail
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer
import com.zj.windmill.R
import com.zj.windmill.data.remote.VideoUrlParser
import com.zj.windmill.databinding.ActivityPlayBinding
import com.zj.windmill.model.Video
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlayActivity : GSYBaseActivityDetail<StandardGSYVideoPlayer>() {

    private lateinit var binding: ActivityPlayBinding

    override fun getGSYVideoPlayer(): StandardGSYVideoPlayer {
        return findViewById(R.id.video_player)
    }

    override fun getGSYVideoOptionBuilder(): GSYVideoOptionBuilder {
        return GSYVideoOptionBuilder()
            .setUrl("http://devimages.apple.com/iphone/samples/bipbop/bipbopall.m3u8")
            .setCacheWithPlay(true)
            .setIsTouchWiget(true)
            .setRotateViewAuto(false)
            .setLockLand(false)
            .setShowFullAnimation(false)
            .setNeedLockFull(true)
    }

    override fun clickForFullScreen() {

    }

    override fun getDetailOrientationRotateAuto(): Boolean {
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}