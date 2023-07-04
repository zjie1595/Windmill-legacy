package com.zj.windmill.model

data class HomePage(
    val bannerVideos: List<Video>,
    val videoGroups: List<VideoGroup>,
    val weeklyRankingVideos: List<Video>
)
