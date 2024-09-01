package ru.robilko.settings.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.robilko.settings.data.repo.AppConfigRepositoryImpl
import ru.robilko.settings.domain.repo.AppConfigRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface SettingsModule {
    @Binds
    @Singleton
    fun bindsAppConfigRepository(repositoryImpl: AppConfigRepositoryImpl): AppConfigRepository
}