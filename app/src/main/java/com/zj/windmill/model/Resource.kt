package com.zj.windmill.model

sealed class Resource

data class Success<T>(
    val data: T
) : Resource()

data class Error(
    val errorMessage: String = "未知错误"
) : Resource()

data class Loading(
    val message: String? = null
) : Resource()