package com.zj.windmill.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.zj.windmill.data.remote.SearchPageParser
import com.zj.windmill.data.remote.SearchPagingSource
import com.zj.windmill.model.Loading
import com.zj.windmill.model.None
import com.zj.windmill.model.PageState
import com.zj.windmill.model.Success
import com.zj.windmill.model.Video
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SearchUiState(
    val pageState: PageState = None,
    val searchResultFlow: Flow<PagingData<Video>> = emptyFlow()
)

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchPageParser: SearchPageParser
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    fun performSearch(keyword: String) {
        _uiState.update {
            it.copy(pageState = Loading)
        }
        viewModelScope.launch(Dispatchers.IO) {
            val searchResultFlow = Pager(PagingConfig(pageSize = 24)) {
                SearchPagingSource(keyword, searchPageParser)
            }.flow
            _uiState.update {
                it.copy(pageState = Success, searchResultFlow = searchResultFlow)
            }
        }
    }
}