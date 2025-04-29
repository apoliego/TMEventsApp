package com.ticketmaster.domain.repository

import com.ticketmaster.domain.model.Event
import com.ticketmaster.domain.utils.Result

interface EventsRepository {
    suspend fun fetchEvents(
    ): Result<List<Event>>
}