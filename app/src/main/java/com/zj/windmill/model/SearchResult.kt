package com.zj.windmill.model

data class SearchResult(
    val videos: List<Video> = emptyList(),
    val hasMore: Boolean = false
)
