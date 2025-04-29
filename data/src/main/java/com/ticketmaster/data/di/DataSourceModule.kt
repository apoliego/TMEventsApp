package com.ticketmaster.data.di

import com.ticketmaster.data.repository.datasource.LocalEventDataSource
import com.ticketmaster.data.repository.datasource.RemoteEventDataSource
import com.ticketmaster.data.repository.datasource.impl.LocalEventDataSourceImpl
import com.ticketmaster.data.repository.datasource.impl.RemoteEventDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {
    @Binds
    @Singleton
    abstract fun bindLocalEventDataSource(
        impl: LocalEventDataSourceImpl
    ): LocalEventDataSource

    @Binds
    @Singleton
    abstract fun bindRemoteEventDataSource(
        impl: RemoteEventDataSourceImpl
    ): RemoteEventDataSource
}