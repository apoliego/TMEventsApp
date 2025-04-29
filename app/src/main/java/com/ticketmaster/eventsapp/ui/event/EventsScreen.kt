package com.ticketmaster.eventsapp.ui.event

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.katas.weatherapp.ui.theme.MyAppTheme
import com.ticketmaster.domain.model.Event
import com.ticketmaster.domain.repository.EventsRepository
import com.ticketmaster.domain.usecases.GetEventsUseCase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventScreen(viewModel: EventsViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    Scaffold(
        modifier = Modifier.windowInsetsPadding(WindowInsets.systemBars),
        topBar = {
            TopAppBar(
                title = { Text("Simple TM Events List") },
                modifier = Modifier.fillMaxWidth()
            )
        }
    ) { paddingValues ->
        // Content based on uiState
        when (uiState) {
            is EventUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is EventUiState.Success -> {
                val events = (uiState as EventUiState.Success).events
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { query ->
                            searchQuery = query
                            viewModel.filterEvents(query)
                        },
                        label = { Text("Search Events") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        singleLine = true
                    )
                    EventList(events = events)
                }
            }

            is EventUiState.Error -> {
                val errorMessage = (uiState as EventUiState.Error).message
                ErrorScreen(
                    message = errorMessage,
                    onRetry = { viewModel.loadEvents() }
                )
            }
        }
    }
}

@Composable
fun EventList(events: List<Event>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(events, key = { it.id }) { event ->
            EventItem(event = event)
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun EventItem(event: Event) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = event.imageUrl,
                contentDescription = "Event image",
                modifier = Modifier
                    .size(64.dp)
                    .padding(end = 16.dp)
            )
            Column {
                Text(text = event.name, style = MaterialTheme.typography.titleMedium)
                Text(text = event.type, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
fun ErrorScreen(message: String, onRetry: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.error
            )
            Button(onClick = onRetry) {
                Text("Retry")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EventScreenPreview() {
    MyAppTheme {
        EventScreen(
            viewModel = EventsViewModel(
                GetEventsUseCase(
                    object : EventsRepository {
                        override suspend fun fetchEvents(): com.ticketmaster.domain.utils.Result<List<Event>> {
                            return com.ticketmaster.domain.utils.Result.Success(
                                listOf(
                                    Event(
                                        "1",
                                        "Sample Event",
                                        "Concert",
                                        "https://example.com/image.jpg"
                                    )
                                )
                            )
                        }
                    }
                )
            )
        )
    }
}