package ru.robilko.base.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.robilko.base.util.DispatchersProvider
import ru.robilko.base.util.DispatchersProviderImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface CoreBaseModule {
    @Binds
    @Singleton
    fun bindDispatchersProvider(impl: DispatchersProviderImpl): DispatchersProvider
}