package ru.robilko.base_games.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.robilko.base_games.data.remote.BaseGamesRemoteDataSource
import ru.robilko.base_games.data.remote.BaseGamesRemoteDataSourceImpl
import ru.robilko.base_games.data.repo.BaseGamesRepositoryImpl
import ru.robilko.base_games.domain.repo.BaseGamesRepository

@Module
@InstallIn(SingletonComponent::class)
interface BaseGamesModule {
    @Binds
    fun bindBaseGamesRemoteDataSource(baseGamesRemoteDataSourceImpl: BaseGamesRemoteDataSourceImpl): BaseGamesRemoteDataSource

    @Binds
    fun bindBaseGamesRepository(baseGamesRepository: BaseGamesRepositoryImpl): BaseGamesRepository
}