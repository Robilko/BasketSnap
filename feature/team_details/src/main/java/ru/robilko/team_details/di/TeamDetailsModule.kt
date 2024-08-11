package ru.robilko.team_details.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.robilko.team_details.data.remote.TeamsDetailsRemoteDataSource
import ru.robilko.team_details.data.remote.TeamsDetailsRemoteDataSourceImpl
import ru.robilko.team_details.data.repo.TeamsDetailsRepositoryImpl
import ru.robilko.team_details.domain.repo.TeamsDetailsRepository

@Module
@InstallIn(SingletonComponent::class)
interface TeamDetailsModule {
    @Binds
    fun bindTeamsDetailsRemoteDataSource(remoteDataSource: TeamsDetailsRemoteDataSourceImpl): TeamsDetailsRemoteDataSource

    @Binds
    fun bindTeamsDetailsRepository(repository: TeamsDetailsRepositoryImpl): TeamsDetailsRepository
}