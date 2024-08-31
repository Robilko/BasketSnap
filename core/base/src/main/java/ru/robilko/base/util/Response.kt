package ru.robilko.base.util

import android.util.Log
import ru.robilko.core_base.BuildConfig

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
        if (BuildConfig.DEBUG) Log.e("TAG", "onFailure error message: ${this.errorMessage.orEmpty()}")
        code(this)
    }
}