package com.zj.windmill.ui.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.zj.windmill.items
import com.zj.windmill.model.Video
import com.zj.windmill.ui.home.VerticalVideo
import com.zj.windmill.ui.widget.SearchBar

@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onVideoClick: (Video) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val lazyPagingItems = uiState.searchResult.collectAsLazyPagingItems()
    Column(modifier = Modifier.fillMaxSize()) {
        SearchBar(
            searchText = uiState.keyword,
            showSoftKeyboard = uiState.showSoftKeyboard,
            onSearchTextChanged = {
                viewModel.onSearchTextChanged(it)
            },
            onBackClick = onBackClick,
            onClearClick = {
                viewModel.clearSearchText()
            },
            onSearch = {
                viewModel.performSearch(uiState.keyword)
            }
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(lazyPagingItems) {
                VerticalVideo(onVideoClick = onVideoClick, video = it)
            }
            if (lazyPagingItems.loadState.append == LoadState.Loading) {
                item {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }
}