package com.zj.windmill.ui.widget

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import com.zj.windmill.model.Video
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

@OptIn(ExperimentalPagerApi::class)
@Composable
fun Banner(
    videos: List<Video>,
    modifier: Modifier = Modifier,
    loopDelayMs: Long = 3000L,
    onVideoClicked: (Video) -> Unit
) {
    val pagerState = rememberPagerState()
    var underDragging by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(key1 = Unit) {
        pagerState.interactionSource.interactions.collect { interaction ->
            when (interaction) {
                is PressInteraction.Press -> underDragging = true
                is PressInteraction.Release -> underDragging = false
                is PressInteraction.Cancel -> underDragging = false
                is DragInteraction.Start -> underDragging = true
                is DragInteraction.Stop -> underDragging = false
                is DragInteraction.Cancel -> underDragging = false
            }
        }
    }
    if (videos.isNotEmpty() && underDragging.not()) {
        LaunchedEffect(videos) {
            while (isActive) {
                delay(loopDelayMs)
                val targetPage = if (pagerState.currentPage + 1 > videos.size - 1) {
                    0
                } else {
                    pagerState.currentPage + 1
                }
                if (targetPage > pagerState.currentPage) {
                    pagerState.animateScrollToPage(targetPage)
                } else {
                    pagerState.scrollToPage(targetPage)
                }
            }
        }
    }
    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        val indicator = createRef()
        HorizontalPager(
            count = videos.size,
            state = pagerState
        ) { page ->
            val video = videos[page]
            ConstraintLayout(
                modifier = modifier.clickable { onVideoClicked.invoke(video) }
            ) {
                val text = createRef()
                AsyncImage(
                    model = video.coverImageUrl,
                    contentDescription = null,
                    modifier = modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(MaterialTheme.shapes.medium),
                    contentScale = ContentScale.Crop
                )
                Text(
                    text = video.title,
                    style = MaterialTheme.typography.subtitle2.copy(color = MaterialTheme.colors.surface),
                    modifier = modifier.constrainAs(text) {
                        bottom.linkTo(parent.bottom, margin = 8.dp)
                        start.linkTo(parent.start, margin = 8.dp)
                    })
            }
        }
        HorizontalPagerIndicator(
            pagerState = pagerState,
            activeColor = MaterialTheme.colors.surface,
            inactiveColor = MaterialTheme.colors.surface.copy(alpha = 0.5F),
            indicatorWidth = 6.dp,
            modifier = modifier.constrainAs(indicator) {
                bottom.linkTo(parent.bottom, margin = 8.dp)
                end.linkTo(parent.end, margin = 8.dp)
            }
        )
    }
}