package com.ticketmaster.eventsapp.ui.event

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ticketmaster.domain.model.Event
import com.ticketmaster.domain.usecases.GetEventsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventsViewModel @Inject constructor(
    private val getEventsUseCase: GetEventsUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<EventUiState>(EventUiState.Loading)
    val uiState: StateFlow<EventUiState> = _uiState.asStateFlow()

    private var _allEvents = listOf<Event>()
    private val _searchQuery = MutableStateFlow("")

    init {
        loadEvents()
    }

    fun loadEvents() {
        viewModelScope.launch {
            _uiState.value = EventUiState.Loading
            when (val result = getEventsUseCase()) {
                is com.ticketmaster.domain.utils.Result.Success -> {
                    _allEvents = result.data
                    updateFilteredEvents()
                }

                is com.ticketmaster.domain.utils.Result.Error -> {
                    val errorMessage = "An error occurred: ${result.exception.message}"
                    _uiState.value = EventUiState.Error(errorMessage)
                }
            }
        }
    }

    fun filterEvents(query: String) {
        _searchQuery.value = query
        updateFilteredEvents()
    }

    private fun updateFilteredEvents() {
        val query = _searchQuery.value.lowercase()
        val filteredEvents = if (query.isEmpty()) {
            _allEvents
        } else {
            _allEvents.filter { event ->
                event.name.lowercase().contains(query) || event.type.lowercase().contains(query)
            }
        }
        _uiState.value = EventUiState.Success(filteredEvents)
    }
}

sealed class EventUiState {
    object Loading : EventUiState()
    data class Success(val events: List<Event>) : EventUiState()
    data class Error(val message: String) : EventUiState()
}