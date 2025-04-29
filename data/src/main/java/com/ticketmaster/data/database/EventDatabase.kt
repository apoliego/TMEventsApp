package com.ticketmaster.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ticketmaster.data.database.dao.EventDao
import com.ticketmaster.data.database.model.EventEntity

@Database(entities = [EventEntity::class], version = 1)
abstract class EventDatabase : RoomDatabase() {
    abstract fun eventDao(): EventDao
}