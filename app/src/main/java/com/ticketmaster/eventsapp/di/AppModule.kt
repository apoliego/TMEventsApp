package com.katas.moviesapp.di

import com.ticketmaster.domain.repository.EventsRepository
import com.ticketmaster.domain.usecases.GetEventsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideGetEventsUseCase(repository: EventsRepository): GetEventsUseCase {
        return GetEventsUseCase(repository)
    }
}