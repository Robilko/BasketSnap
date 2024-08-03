package ru.robilko.base_favourites.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.robilko.base_favourites.data.local.BaseFavouritesLocalDataSource
import ru.robilko.base_favourites.data.local.BaseFavouritesLocalDataSourceImpl
import ru.robilko.base_favourites.data.repo.BaseFavouritesRepositoryImpl
import ru.robilko.base_favourites.domain.repo.BaseFavouritesRepository

@Module
@InstallIn(SingletonComponent::class)
interface BaseFavouritesModule {
    @Binds
    fun bindsBaseFavouritesLocalDataSource(dataSourceImpl: BaseFavouritesLocalDataSourceImpl): BaseFavouritesLocalDataSource

    @Binds
    fun bindsBaseFavouritesRepository(repositoryImpl: BaseFavouritesRepositoryImpl): BaseFavouritesRepository
}