package com.swipe.catalog.network

sealed class ApiResponse<out T> {
    data class Success<out T>(val data: T) : ApiResponse<T>()
    data class Error(val errorMessage: String) : ApiResponse<Nothing>()
    data object Loading : ApiResponse<Nothing>()

    companion object {
        fun <T> success(data: T): ApiResponse<T> {
            return Success(data)
        }

        fun error(errorMessage: String): ApiResponse<Nothing> {
            return Error(errorMessage)
        }

        fun loading(): ApiResponse<Nothing> {
            return Loading
        }
    }
}
