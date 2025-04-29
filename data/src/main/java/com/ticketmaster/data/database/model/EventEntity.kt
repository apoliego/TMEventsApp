package com.ticketmaster.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ticketmaster.domain.model.Event

@Entity(tableName = "events")
data class EventEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val type: String,
    val imageUrl: String,
) {
    fun toDomain(): Event = Event(
        id = id,
        name = name,
        type = type,
        imageUrl = imageUrl
    )
}

fun Event.toEntity(): EventEntity = EventEntity(
    id = id,
    name = name,
    type = type,
    imageUrl = imageUrl
)