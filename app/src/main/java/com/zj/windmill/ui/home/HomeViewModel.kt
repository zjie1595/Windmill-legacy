package com.zj.windmill.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zj.windmill.data.remote.HomePageParser
import com.zj.windmill.model.Error
import com.zj.windmill.model.HomePage
import com.zj.windmill.model.Loading
import com.zj.windmill.model.PageState
import com.zj.windmill.model.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val pageState: PageState = Loading,
    val homePage: HomePage = HomePage()
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homePageParser: HomePageParser
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val homePage = homePageParser.parseHomePage()
            if (homePage == null) {
                _uiState.update {
                    it.copy(pageState = Error())
                }
            } else {
                _uiState.update {
                    it.copy(pageState = Success, homePage = homePage)
                }
            }
        }
    }
}