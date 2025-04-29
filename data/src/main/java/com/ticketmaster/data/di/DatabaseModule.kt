package com.ticketmaster.data.di

import android.content.Context
import androidx.room.Room
import com.ticketmaster.data.database.EventDatabase
import com.ticketmaster.data.database.dao.EventDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): EventDatabase =
        Room.databaseBuilder(
            context,
            EventDatabase::class.java,
            "event_database"
        ).build()

    @Provides
    @Singleton
    fun provideEventDao(database: EventDatabase): EventDao = database.eventDao()
}