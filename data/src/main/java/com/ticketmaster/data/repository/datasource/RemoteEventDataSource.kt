package com.ticketmaster.data.repository.datasource

import com.ticketmaster.domain.model.Event

interface RemoteEventDataSource {
    suspend fun getEvents(): List<Event>
}