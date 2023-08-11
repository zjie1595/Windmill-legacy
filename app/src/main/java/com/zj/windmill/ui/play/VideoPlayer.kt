package com.zj.windmill.ui.play

interface VideoPlayer {

    fun contentPosition(): Long

    fun contentDuration(): Long

    /**
     * 播放视频
     * @param [videoUrl] 视频网址
     * @param [title] 标题
     * @param [contentPosition] 内容位置
     */
    fun playVideo(videoUrl: String, title: String? = null, contentPosition: Long? = null)

    /**
     * 显示解析错误
     * @param [throwable] throwable
     */
    fun showParseError(throwable: Throwable)

    /**
     * 资源解析中
     */
    fun showParsing()

    /**
     * 显示未知错误
     */
    fun showUnknownError()
}