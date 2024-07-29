package ru.robilko.home.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import ru.robilko.home.data.local.HomeLocalDataSource
import ru.robilko.home.data.local.HomeLocalDataSourceImpl
import ru.robilko.home.data.remote.HomeApi
import ru.robilko.home.data.remote.HomeRemoteDataSource
import ru.robilko.home.data.remote.HomeRemoteDataSourceImpl
import ru.robilko.home.data.repo.HomeRepositoryImpl
import ru.robilko.home.domain.repo.HomeRepository
import ru.robilko.remote.di.RetrofitApiBasketball
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface HomeModule {
    companion object {
        @Singleton
        @Provides
        fun provideHomeApi(
            @RetrofitApiBasketball retrofit: Retrofit
        ): HomeApi = retrofit.create(HomeApi::class.java)
    }

    @Binds
    fun bindHomeLocalDataSource(impl: HomeLocalDataSourceImpl): HomeLocalDataSource

    @Binds
    fun bindHomeRemoteDataSource(impl: HomeRemoteDataSourceImpl): HomeRemoteDataSource

    @Binds
    fun bindHomeRepository(impl: HomeRepositoryImpl): HomeRepository
}