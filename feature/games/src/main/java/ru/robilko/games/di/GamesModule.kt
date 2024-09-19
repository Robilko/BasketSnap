package ru.robilko.games.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.robilko.games.data.remote.GamesRemoteDataSource
import ru.robilko.games.data.remote.GamesRemoteDataSourceImpl
import ru.robilko.games.data.repo.GamesRepositoryImpl
import ru.robilko.games.domain.repo.GamesRepository

@Module
@InstallIn(SingletonComponent::class)
interface GamesModule {
    @Binds
    fun bindGamesRemoteDataSource(gamesRemoteDataSourceImpl: GamesRemoteDataSourceImpl): GamesRemoteDataSource

    @Binds
    fun bindGamesRepository(gamesRepository: GamesRepositoryImpl): GamesRepository
}