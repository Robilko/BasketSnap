package ru.robilko.leagues.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.robilko.leagues.data.remote.LeaguesRemoteDataSource
import ru.robilko.leagues.data.remote.LeaguesRemoteDataSourceImpl
import ru.robilko.leagues.data.repo.LeaguesRepositoryImpl
import ru.robilko.leagues.domain.repo.LeaguesRepository

@Module
@InstallIn(SingletonComponent::class)
interface LeaguesModule {
    @Binds
    fun bindsLeaguesRemoteDataSource(remoteDataSourceImpl: LeaguesRemoteDataSourceImpl): LeaguesRemoteDataSource

    @Binds
    fun bindsLeaguesRepository(repositoryImpl: LeaguesRepositoryImpl): LeaguesRepository
}