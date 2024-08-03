package ru.robilko.remote.util

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import ru.robilko.base.util.Response
import java.io.IOException
import java.net.SocketTimeoutException

interface SafeApiCall {
    suspend fun <T> safeApiCall(
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        apiCall: suspend () -> T
    ): Response<T> {
        return withContext(dispatcher) {
            try {
                Response.Success(apiCall.invoke())
            } catch (throwable: Throwable) {
                when (throwable) {
                    is HttpException -> {
                        Response.Failure(
                            false,
                            throwable.code(),
                            "${throwable.response()?.errorBody()?.string()}"
                        )
                    }

                    is SocketTimeoutException -> {
                        Response.Failure(
                            true,
                            null,
                            "SocketTimeoutException: ${throwable.localizedMessage}"
                        )
                    }

                    is IOException -> {
                        Response.Failure(
                            true,
                            null,
                            "IOException: ${throwable.localizedMessage}"
                        )
                    }

                    else -> {
                        Response.Failure(
                            false,
                            null,
                            throwable.localizedMessage
                        )
                    }
                }
            }
        }
    }
}