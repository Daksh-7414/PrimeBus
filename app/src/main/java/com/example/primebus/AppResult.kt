package com.example.primebus

sealed class AppResult<out T> {

    object Loading : AppResult<Nothing>()

    data class Success<T>(val data: T) : AppResult<T>()

    data class Error(val message: String) : AppResult<Nothing>()

    object NoInternet : AppResult<Nothing>()
}