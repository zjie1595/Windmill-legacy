package com.zj.windmill.ui.play

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zj.windmill.data.remote.DetailPageParser
import com.zj.windmill.model.Video
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PlayViewModel @Inject constructor(
    private val detailPageParser: DetailPageParser
) : ViewModel() {

    fun parsePlayPageInfo(video: Video) {
        viewModelScope.launch {
            val detailPage = withContext(Dispatchers.IO) {
                detailPageParser.parseDetailPage(video.detailPageUrl)
            }
        }
    }
}