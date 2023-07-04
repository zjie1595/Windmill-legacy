package com.zj.windmill.model

data class HomePage(
    val bannerVideos: List<Video> = emptyList(),
    val videoGroups: List<Any> = emptyList(),
    val weeklyRankingVideos: List<Video> = emptyList()
)
