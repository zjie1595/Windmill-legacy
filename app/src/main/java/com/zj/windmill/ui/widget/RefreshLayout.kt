package com.zj.windmill.ui.widget

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.delay

// 页面状态
const val PAGE_LOADING = 0
const val PAGE_ERROR = 1
const val PAGE_EMPTY = 2
const val PAGE_SUCCESS = 3

// 下拉刷新状态
const val REFRESHING = 4            // 刷新中...
const val REFRESH_ERROR = 5         // 刷新失败
const val REFRESH_SUCCESS = 6       // 刷新成功
const val REFRESH_EMPTY = 7         // 刷新无数据

// 上拉加载状态
const val MORE_LOADING = 8          // 更多加载中...
const val MORE_LOADING_ERROR = 9    // 更多加载失败
const val MORE_LOADING_EMPTY = 10    // 已无更多
const val MORE_LOADING_SUCCESS = 11  // 更多加载成功

@Composable
fun RefreshLayout(
    modifier: Modifier = Modifier,
    enableRefresh: Boolean = true,
    enableLoadMore: Boolean = true,
    state: Int = PAGE_LOADING,
    onRefresh: (() -> Unit)? = null,
    onLoadMore: (() -> Unit)? = null,
    content: LazyGridScope.() -> Unit
) {
    val refreshState = rememberSwipeRefreshState(isRefreshing = state == REFRESHING)
    SwipeRefresh(
        state = refreshState,
        onRefresh = { onRefresh?.invoke() },
        swipeEnabled = enableRefresh
    ) {
        LazyVerticalGrid(columns = GridCells.Fixed(3)) {
            item {
                Crossfade(targetState = state) { state->
                    when (state) {
                        PAGE_LOADING -> {
                            Box {
                                CircularProgressIndicator()
                            }
                        }
                        PAGE_EMPTY -> {

                        }
                        PAGE_ERROR -> {

                        }
                        else -> {

                        }
                    }
                }
            }
            if (enableLoadMore && state == MORE_LOADING) {
                item {
                    Text(
                        text = "加载中...", modifier = modifier
                            .fillMaxWidth()
                            .height(56.dp)
                    )
                }
            }
            if (enableLoadMore && state == MORE_LOADING_ERROR) {
                item {
                    Text(
                        text = "加载失败", modifier = modifier
                            .fillMaxWidth()
                            .height(56.dp)
                    )
                }
            }
            if (enableLoadMore && state == MORE_LOADING_EMPTY) {
                item {
                    Text(
                        text = "已无更多数据", modifier = modifier
                            .fillMaxWidth()
                            .height(56.dp)
                    )
                }
            }
            if (enableLoadMore && state == MORE_LOADING_SUCCESS) {
                item {
                    var visible: Boolean by remember { mutableStateOf(true) }
                    LaunchedEffect(key1 = Unit) {
                        delay(1000L)
                        visible = false
                    }
                    AnimatedVisibility(visible = visible) {
                        Text(
                            text = "加载成功", modifier = modifier
                                .fillMaxWidth()
                                .height(56.dp)
                        )
                    }
                }
            }
        }
    }
}