package ru.robilko.base_seasons.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.robilko.base_seasons.data.remote.BaseSeasonsRemoteDataSource
import ru.robilko.base_seasons.data.remote.BaseSeasonsRemoteDataSourceImpl
import ru.robilko.base_seasons.data.repo.BaseSeasonsRepositoryImpl
import ru.robilko.base_seasons.domain.repo.BaseSeasonsRepository

@Module
@InstallIn(SingletonComponent::class)
interface BaseSeasonsModule {
    @Binds
    fun bindBaseSeasonsRemoteDataSource(baseSeasonsRemoteDataSourceImpl: BaseSeasonsRemoteDataSourceImpl): BaseSeasonsRemoteDataSource

    @Binds
    fun bindBaseSeasonsRepository(baseSeasonsRepository: BaseSeasonsRepositoryImpl): BaseSeasonsRepository
}