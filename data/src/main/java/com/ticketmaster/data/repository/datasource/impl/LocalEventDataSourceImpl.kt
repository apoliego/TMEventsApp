package com.ticketmaster.data.repository.datasource.impl

import com.ticketmaster.data.database.dao.EventDao
import com.ticketmaster.data.database.model.toEntity
import com.ticketmaster.data.repository.datasource.LocalEventDataSource
import com.ticketmaster.domain.model.Event
import javax.inject.Inject

class LocalEventDataSourceImpl @Inject constructor(
    private val eventDao: EventDao
) : LocalEventDataSource {
    override suspend fun getEvents(): List<Event> =
        eventDao.getAllEvents().map { it.toDomain() }

    override suspend fun saveEvents(events: List<Event>) {
        eventDao.insertEvents(events.map { it.toEntity() })
    }

    override suspend fun clearEvents() {
        eventDao.clearEvents()
    }
}