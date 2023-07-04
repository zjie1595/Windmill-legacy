package com.zj.windmill.ui.play

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zj.windmill.data.remote.DetailPageParser
import com.zj.windmill.data.remote.VideoUrlParser
import com.zj.windmill.model.Playlist
import com.zj.windmill.model.Video
import com.zj.windmill.ui.widget.ERROR
import com.zj.windmill.ui.widget.LOADING
import com.zj.windmill.ui.widget.NONE
import com.zj.windmill.ui.widget.SUCCESS
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

data class PlayUiState(
    val videoUrl: String? = null,
    val playlists: List<Playlist> = emptyList(),
    val state: Int = NONE
)

@HiltViewModel
class PlayViewModel @Inject constructor(
    private val detailPageParser: DetailPageParser
) : ViewModel() {

    private val _uiState: MutableStateFlow<PlayUiState> = MutableStateFlow(PlayUiState())
    val uiState: StateFlow<PlayUiState> = _uiState

    private var init = false

    fun init(video: Video, videoUrlParser: VideoUrlParser) {
        if (init) {
            return
        }
        init = true
        _uiState.update {
            it.copy(state = LOADING)
        }
        viewModelScope.launch(Dispatchers.IO) {
            val detailPage = detailPageParser.parseDetailPage(video.detailPageUrl)
            if (detailPage == null) {
                _uiState.update {
                    it.copy(state = ERROR)
                }
                return@launch
            }
            _uiState.update {
                it.copy(playlists = detailPage.playlists, state = SUCCESS)
            }
            val defaultPlayIndex = detailPage.defaultPlayIndex
            val playlist = detailPage.playlists.getOrNull(defaultPlayIndex)
            val episode = playlist?.episodes?.getOrNull(0)
            val playPageUrl = episode?.playPageUrl ?: return@launch
            val videoUrl = withContext(Dispatchers.Main) {
                videoUrlParser.parseVideoUrl(playPageUrl)
            }
            _uiState.update {
                it.copy(videoUrl = videoUrl)
            }
        }
    }
}