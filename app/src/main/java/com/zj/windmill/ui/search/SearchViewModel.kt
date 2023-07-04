package com.zj.windmill.ui.search

import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.zj.windmill.data.remote.SearchPageParser
import com.zj.windmill.data.remote.SearchPagingSource
import com.zj.windmill.model.Video
import com.zj.windmill.ui.widget.NONE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class SearchUiState(
    val keyword: String = "",
    val state: Int = NONE,
    val searchResult: Flow<PagingData<Video>> = emptyFlow(),
    val showSoftKeyboard: Boolean = true
)

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchPageParser: SearchPageParser
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState

    fun performSearch(keyword: String) {
        val searchResult = Pager(PagingConfig(24)) {
            SearchPagingSource(keyword, searchPageParser)
        }.flow
        _uiState.update {
            it.copy(searchResult = searchResult, keyword = keyword)
        }
    }

    fun clearSearchText() {
        _uiState.update {
            it.copy(keyword = "")
        }
    }

    fun onSearchTextChanged(newText: String) {
        _uiState.update {
            it.copy(keyword = newText)
        }
    }
}