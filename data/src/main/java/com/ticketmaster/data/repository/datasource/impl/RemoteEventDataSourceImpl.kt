package com.ticketmaster.data.repository.datasource.impl

import com.ticketmaster.data.network.api.EventsApi
import com.ticketmaster.data.network.api.EventsApi.Companion.API_KEY
import com.ticketmaster.data.repository.datasource.RemoteEventDataSource
import com.ticketmaster.domain.model.Event
import javax.inject.Inject

class RemoteEventDataSourceImpl @Inject constructor(
    private val eventsApi: EventsApi
) : RemoteEventDataSource {
    override suspend fun getEvents(): List<Event> =
        eventsApi.fetchEvents(API_KEY).toDomain()
}