package com.zj.windmill.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.zj.windmill.R
import com.zj.windmill.model.Video
import com.zj.windmill.ui.widget.Banner
import com.zj.windmill.ui.widget.StateLayout

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onVideoClick: (Video) -> Unit,
    onSearchClick: () -> Unit,
    onHistoryClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.app_name))
                },
                actions = {
                    IconButton(onClick = { onHistoryClick() }) {
                        Icon(
                            painterResource(id = R.drawable.history),
                            contentDescription = stringResource(R.string.watch_history)
                        )
                    }
                    IconButton(onClick = { onSearchClick() }) {
                        Icon(
                            Icons.Filled.Search,
                            contentDescription = stringResource(R.string.search_animation)
                        )
                    }
                }
            )
        }
    ) {
        StateLayout(state = uiState.state, modifier = Modifier.padding(it)) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item(span = { GridItemSpan(3) }) {
                    Banner(videos = uiState.homePage.bannerVideos, onVideoClicked = onVideoClick)
                }
                items(uiState.homePage.videoGroups, span = { model ->
                    if (model is Video) {
                        GridItemSpan(1)
                    } else {
                        GridItemSpan(3)
                    }
                }) { model ->
                    when (model) {
                        is Video -> {
                            VerticalVideo(onVideoClick = onVideoClick, video = model)
                        }

                        is String -> {
                            Text(text = model, style = MaterialTheme.typography.subtitle1)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun VerticalVideo(
    onVideoClick: (Video) -> Unit,
    video: Video
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onVideoClick.invoke(video) }
    ) {
        AsyncImage(
            model = video.coverImageUrl,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .clip(MaterialTheme.shapes.medium),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = video.title,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.subtitle2
        )
    }
}