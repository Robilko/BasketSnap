package ru.robilko.remote.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.robilko.remote.ApiBasketballInterceptor
import ru.robilko.remote.BuildConfig
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

    @Singleton
    @Provides
    @RetrofitApiBasketball
    fun provideApiBasketballRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .baseUrl(API_BASKETBALL_BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private const val API_BASKETBALL_BASE_URL = "https://api-basketball.p.rapidapi.com/"
}

@Qualifier
annotation class RetrofitApiBasketball