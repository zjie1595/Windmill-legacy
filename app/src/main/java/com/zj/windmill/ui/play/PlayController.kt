package com.zj.windmill.ui.play

import com.zj.windmill.data.local.AppDatabase
import com.zj.windmill.data.remote.DetailPageParser
import com.zj.windmill.data.remote.VideoUrlParser
import com.zj.windmill.model.Episode
import com.zj.windmill.model.Playlist
import com.zj.windmill.model.Video
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.threeten.bp.LocalDateTime

class PlayController(
    private val videoUrlParser: VideoUrlParser,
    private val detailPageParser: DetailPageParser,
    private val database: AppDatabase,
    private val videoPlayer: VideoPlayer
) {

    private val episodeDao by lazy { database.episodeDao() }
    private val videoDao by lazy { database.videoDao() }

    private val playlists = mutableListOf<Playlist>()

    private var currentVideo: Video? = null

    private var currentPlaylist: Playlist? = null

    private var currentEpisode: Episode? = null

    var onPlaylistsParsed: ((List<Playlist>) -> Unit)? = null

    private var currentEpisodePosition = -1

    private var currentPlaylistPosition = -1

    suspend fun setupVideo(video: Video?) {
        videoPlayer.showParsing()
        currentVideo = video?.let {
            saveAndGetVideo(it)
        }
        val detailPageUrl = currentVideo?.detailPageUrl
        if (detailPageUrl == null) {
            videoPlayer.showUnknownError()
            return
        }
        val detailPage = withContext(Dispatchers.IO) {
            detailPageParser.parseDetailPage(detailPageUrl)
        }
        detailPage?.playlists?.let {
            onPlaylistsParsed?.invoke(it)
            updatePlaylist(currentVideo?.playlistIndex ?: 0)
            updateEpisode(currentVideo?.episodeIndex ?: 0)
            playVideo()
        }
    }

    /**
     * 切换播放列表
     * @param [playlistPosition] 播放列表位置
     */
    suspend fun switchPlaylist(playlistPosition: Int) {
        saveCurrentEpisode()
        updatePlaylist(playlistPosition)
        updateEpisode(0)
        playVideo()
    }

    /**
     * 换集
     * @param [episodePosition] 集位置
     */
    suspend fun switchEpisode(episodePosition: Int) {
        saveCurrentEpisode()
        updateEpisode(episodePosition)
        playVideo()
    }

    /**
     * 下一集
     */
    suspend fun next() {
        val episodeMaxPosition = currentPlaylist?.episodes?.size?.minus(1) ?: 0
        if (currentEpisodePosition < episodeMaxPosition) {
            updateEpisode(currentEpisodePosition + 1)
            playVideo()
        }
    }

    /**
     * 退出播放页面
     */
    suspend fun exitPage() {
        saveVideo()
    }

    /**
     * 保存视频信息
     */
    private suspend fun saveVideo() {
        currentVideo?.playlistIndex = currentPlaylistPosition
        currentVideo?.episodeIndex = currentEpisodePosition
        currentVideo?.watchTime = LocalDateTime.now()
        currentVideo?.contentPosition = videoPlayer.contentPosition()
        currentVideo?.contentDuration = videoPlayer.contentDuration()
        currentVideo?.let {
            videoDao.insert(it)
        }
    }

    private suspend fun saveAndGetVideo(video: Video?): Video? {
        val title = video?.title ?: return null
        val cacheVideo = videoDao.getByTitle(title)
        if (cacheVideo != null) {
            return cacheVideo
        }
        val videoId = videoDao.insert(video)
        return videoDao.getById(videoId)
    }

    private fun updateEpisode(position: Int) {
        currentEpisodePosition = position
        currentEpisode = currentPlaylist?.episodes?.getOrNull(position)
    }

    private fun updatePlaylist(position: Int) {
        currentPlaylistPosition = position
        currentPlaylist = playlists.getOrNull(position)
    }

    private suspend fun playVideo() {
        val playPageUrl = currentEpisode?.playPageUrl ?: return
        videoPlayer.showParsing()
        val position = episodeDao.getEpisode(playPageUrl)?.contentPosition ?: 0
        videoUrlParser.parseVideoUrl(playPageUrl).onSuccess { videoUrl ->
            videoPlayer.playVideo(videoUrl, currentEpisode?.title, position)
        }.onFailure { throwable ->
            videoPlayer.showParseError(throwable)
        }
    }

    /**
     * 保存当前的剧情信息
     */
    private suspend fun saveCurrentEpisode() {
        currentEpisode?.let {
            it.contentPosition = videoPlayer.contentPosition()
            it.contentDuration = videoPlayer.contentDuration()
            episodeDao.insert(it)
        }
    }

    /**
     * 是否有下一集
     * @return [Boolean]
     */
    fun hasNextEpisode(): Boolean {
        // 因为这里是提前判断，所以这里是减2
        val episodeMaxPosition = currentPlaylist?.episodes?.size?.minus(2) ?: 0
        return currentEpisodePosition < episodeMaxPosition
    }
}