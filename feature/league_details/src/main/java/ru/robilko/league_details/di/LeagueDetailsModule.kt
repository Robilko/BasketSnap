package ru.robilko.league_details.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.robilko.league_details.data.remote.LeagueDetailsRemoteDataSource
import ru.robilko.league_details.data.remote.LeagueDetailsRemoteDataSourceImpl
import ru.robilko.league_details.data.repo.LeagueDetailsRepositoryImpl
import ru.robilko.league_details.domain.repo.LeagueDetailsRepository

@Module
@InstallIn(SingletonComponent::class)
interface LeagueDetailsModule {
    @Binds
    fun bindsLeagueDetailsRemoteDataSource(remoteDataSourceImpl: LeagueDetailsRemoteDataSourceImpl): LeagueDetailsRemoteDataSource

    @Binds
    fun bindsLeagueDetailsRepository(repositoryImpl: LeagueDetailsRepositoryImpl): LeagueDetailsRepository
}