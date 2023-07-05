package com.zj.windmill.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class SearchUiState(
    val pageState: PageState = None,
    val searchResultFlow: Flow<PagingData<Video>> = emptyFlow(),
    val queryText: String? = null,
    val showSoftKeyboard: Boolean = true
)

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchPageParser: SearchPageParser
) : ViewModel() {

    private val uiState = MutableStateFlow(SearchUiState())

    val pageState = uiState.map { it.pageState }.asLiveData()

    val searchResultFlow = uiState.map { it.searchResultFlow }.asLiveData()

    val queryText = uiState.map { it.queryText }.asLiveData()

    val showSoftKeyboard = uiState.map { it.showSoftKeyboard }.asLiveData()

    private fun performSearch(keyword: String) {
        uiState.update {
            it.copy(pageState = Loading)
        }
        val searchResultFlow = Pager(PagingConfig(pageSize = 24)) {
            SearchPagingSource(keyword, searchPageParser)
        }.flow
        uiState.update {
            it.copy(pageState = Success, searchResultFlow = searchResultFlow)
        }
    }

    fun performSearch() {
        val keyword = queryText.value ?: return
        performSearch(keyword)
        uiState.update {
            it.copy(showSoftKeyboard = false)
        }
    }

    fun onQueryTextUpdate(queryText: CharSequence) {
        uiState.update {
            it.copy(queryText = queryText.toString())
        }
    }
}