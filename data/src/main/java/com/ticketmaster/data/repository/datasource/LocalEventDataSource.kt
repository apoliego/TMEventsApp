package com.ticketmaster.data.repository.datasource

import com.ticketmaster.domain.model.Event

interface LocalEventDataSource {
    suspend fun getEvents(): List<Event>
    suspend fun saveEvents(events: List<Event>)
    suspend fun clearEvents()
}