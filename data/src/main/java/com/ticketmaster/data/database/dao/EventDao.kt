package com.ticketmaster.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ticketmaster.data.database.model.EventEntity

@Dao
interface EventDao {
    @Query("SELECT * FROM events")
    suspend fun getAllEvents(): List<EventEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvents(events: List<EventEntity>)

    @Query("DELETE FROM events")
    suspend fun clearEvents()
}
