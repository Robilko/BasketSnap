package ru.robilko.remote.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import ru.robilko.remote.ApiBasketballInterceptor
import ru.robilko.remote.BuildConfig
import ru.robilko.remote.data.BasketballApi
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {
    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(ApiBasketballInterceptor())
            .addInterceptor(
                HttpLoggingInterceptor()
                    .apply { if (BuildConfig.DEBUG) setLevel(HttpLoggingInterceptor.Level.BODY) }
            )
            .build()

    @OptIn(ExperimentalSerializationApi::class)
    @Singleton
    @Provides
    fun provideJsonConverterFactory(): Converter.Factory {
        val json = Json {
            ignoreUnknownKeys = true
            explicitNulls = false
        }
        return json.asConverterFactory("application/json".toMediaType())
    }

    @Singleton
    @Provides
    @RetrofitApiBasketball
    fun provideApiBasketballRetrofit(
        jsonConverterFactory: Converter.Factory,
        okHttpClient: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .baseUrl(API_BASKETBALL_BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(jsonConverterFactory)
        .build()

    private const val API_BASKETBALL_BASE_URL = "https://api-basketball.p.rapidapi.com/"

    @Singleton
    @Provides
    fun provideBasketballApi(
        @RetrofitApiBasketball retrofit: Retrofit
    ): BasketballApi = retrofit.create(BasketballApi::class.java)
}

@Qualifier
annotation class RetrofitApiBasketball