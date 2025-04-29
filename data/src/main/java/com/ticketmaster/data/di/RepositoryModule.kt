package com.ticketmaster.data.di

import com.ticketmaster.data.repository.EventsRepositoryImpl
import com.ticketmaster.data.repository.datasource.LocalEventDataSource
import com.ticketmaster.data.repository.datasource.RemoteEventDataSource
import com.ticketmaster.domain.repository.EventsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideEventRepository(
        remoteDataSource: RemoteEventDataSource,
        localDataSource: LocalEventDataSource
    ): EventsRepository = EventsRepositoryImpl(remoteDataSource, localDataSource)
}