package com.zj.windmill.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zj.windmill.data.remote.HomePageParser
import com.zj.windmill.model.HomePage
import com.zj.windmill.ui.widget.ERROR
import com.zj.windmill.ui.widget.LOADING
import com.zj.windmill.ui.widget.NONE
import com.zj.windmill.ui.widget.SUCCESS
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val homePage: HomePage = HomePage(),
    val state: Int = NONE
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homePageParser: HomePageParser
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        init()
    }

    private fun init() {
        _uiState.update {
            it.copy(state = LOADING)
        }
        viewModelScope.launch(Dispatchers.IO) {
            val homePage = homePageParser.parseHomePage()
            if (homePage != null) {
                _uiState.update {
                    it.copy(homePage = homePage, state = SUCCESS)
                }
            } else {
                _uiState.update {
                    it.copy(state = ERROR)
                }
            }
        }
    }
}