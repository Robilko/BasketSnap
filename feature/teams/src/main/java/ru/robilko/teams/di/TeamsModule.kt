package ru.robilko.teams.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.robilko.teams.data.remote.TeamsRemoteDataSource
import ru.robilko.teams.data.remote.TeamsRemoteDataSourceImpl
import ru.robilko.teams.data.repo.TeamsRepositoryImpl
import ru.robilko.teams.domain.repo.TeamsRepository

@Module
@InstallIn(SingletonComponent::class)
interface TeamsModule {
    @Binds
    fun bindsTeamsRemoteDataSource(remoteDataSource: TeamsRemoteDataSourceImpl): TeamsRemoteDataSource

    @Binds
    fun bindsTeamsRepository(repository: TeamsRepositoryImpl): TeamsRepository
}