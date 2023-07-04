package com.zj.windmill.model

sealed class PageState

object Success : PageState()

object Loading : PageState()

data class Error(
    val errorMessage: String = "网络异常"
) : PageState()
