package ru.robilko.remote

import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.Response

class ApiBasketballInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestWithHeaders = originalRequest.newBuilder()
            .headers(
                Headers.Builder()
                    .add(RAPID_API_KEY_NAME, getBasketApiKey())
                    .add(RAPID_API_HOST_NAME, RAPID_API_HOST_VALUE)
                    .build()
            )
            .build()
        return chain.proceed(requestWithHeaders)
    }

    private fun getBasketApiKey(): String = BuildConfig.RAPID_API_KEY

    private companion object {
        const val RAPID_API_KEY_NAME = "x-rapidapi-key"
        const val RAPID_API_HOST_NAME = "x-rapidapi-host"
        const val RAPID_API_HOST_VALUE = "api-basketball.p.rapidapi.com"
    }
}