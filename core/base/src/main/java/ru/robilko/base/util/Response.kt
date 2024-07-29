package ru.robilko.base.util

sealed class Response<out T> {
    data class Success<out T>(val data: T) : Response<T>()

    data class Failure(
        val isNetworkError: Boolean,
        val errorCode: Int?,
        val errorMessage: String?
    ) : Response<Nothing>()

    data object Loading : Response<Nothing>()
}

inline fun <T> Response<T>.onSuccess(code: (Response.Success<T>) -> Unit) {
    if (this is Response.Success) {
        code(this)
    }
}

inline fun <T> Response<T>.onFailure(code: (Response.Failure) -> Unit) {
    if (this is Response.Failure) {
        code(this)
    }
}