package com.ticketmaster.domain.usecases

import com.ticketmaster.domain.model.Event
import com.ticketmaster.domain.repository.EventsRepository
import com.ticketmaster.domain.utils.Result

class GetEventsUseCase(private val repository: EventsRepository) {
    suspend operator fun invoke(): Result<List<Event>> {
        return repository.fetchEvents()
    }
}