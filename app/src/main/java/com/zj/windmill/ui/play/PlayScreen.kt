package com.zj.windmill.ui.play

import android.view.SurfaceView
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.zj.windmill.data.remote.VideoUrlParser
import com.zj.windmill.model.Video

@Composable
fun PlayScreen(
    video: Video,
    viewModel: PlayViewModel = hiltViewModel(),
    videoUrlParser: VideoUrlParser
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val playerView = remember {
        SurfaceView(context)
    }
    val player = remember {
        val exoPlayer = ExoPlayer.Builder(context).build()
        exoPlayer.setVideoSurfaceView(playerView)
        exoPlayer
    }
    if (uiState.videoUrl != null) {
        val mediaItem = MediaItem.fromUri(uiState.videoUrl!!)
        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()
    }
    AndroidView(
        factory = { playerView },
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    )
    viewModel.init(video, videoUrlParser)
}