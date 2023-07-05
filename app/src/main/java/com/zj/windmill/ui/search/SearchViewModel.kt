package com.zj.windmill.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.zj.windmill.data.remote.SearchPageParser
import com.zj.windmill.model.Error
import com.zj.windmill.model.Loading
import com.zj.windmill.model.None
import com.zj.windmill.model.PageState
import com.zj.windmill.model.Success
import com.zj.windmill.model.Video
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SearchUiState(
    val pageState: PageState = None,
    val queryText: String? = null,
    val showSoftKeyboard: Boolean = true,
    val searchResult: List<Video> = emptyList(),
    val hasMore: Boolean = false
)

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchPageParser: SearchPageParser
) : ViewModel() {

    private val uiState = MutableStateFlow(SearchUiState())

    val pageState = uiState.map { it.pageState }.asLiveData()

    val searchResult = uiState.map { it.searchResult }.asLiveData()

    val queryText = uiState.map { it.queryText }.asLiveData()

    val showSoftKeyboard = uiState.map { it.showSoftKeyboard }.asLiveData()

    val hasMore = uiState.map { it.hasMore }.asLiveData()

    private fun performSearch(keyword: String) {
        uiState.update {
            it.copy(pageState = Loading)
        }
        viewModelScope.launch(Dispatchers.IO) {
            val result = searchPageParser.parseSearchPage(keyword, 0)
            if (result == null) {
                uiState.update {
                    it.copy(pageState = Error())
                }
            } else {
                uiState.update {
                    it.copy(
                        pageState = Success,
                        searchResult = result.videos,
                        hasMore = result.hasMore
                    )
                }
            }
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