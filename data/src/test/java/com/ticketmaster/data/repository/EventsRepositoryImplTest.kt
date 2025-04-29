package com.ticketmaster.data.repository

import com.ticketmaster.data.database.model.EventEntity
import com.ticketmaster.data.repository.datasource.LocalEventDataSource
import com.ticketmaster.data.repository.datasource.RemoteEventDataSource
import com.ticketmaster.domain.model.Event
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import java.io.IOException

class EventRepositoryImplTest {

    private lateinit var remoteDataSource: RemoteEventDataSource
    private lateinit var localDataSource: LocalEventDataSource
    private lateinit var repository: EventsRepositoryImpl

    @Before
    fun setUp() {
        remoteDataSource = mockk()
        localDataSource = mockk()
        repository = EventsRepositoryImpl(remoteDataSource, localDataSource)
    }

    @Test
    fun `fetchEvents returns remote data when available and caches it`() = runTest {
        // Arrange
        val remoteEvents = listOf(
            Event("1", "Concert", "event", "remote_url")
        )
        coEvery { remoteDataSource.getEvents() } returns remoteEvents
        coEvery { localDataSource.clearEvents() } just runs
        coEvery { localDataSource.saveEvents(remoteEvents) } just runs

        // Act
        val result = repository.fetchEvents()

        // Assert
        assertTrue(result is com.ticketmaster.domain.utils.Result.Success)
        assertEquals(remoteEvents, (result as com.ticketmaster.domain.utils.Result.Success).data)
        coVerify { localDataSource.clearEvents() }
        coVerify { localDataSource.saveEvents(remoteEvents) }
    }

    @Test
    fun `fetchEvents falls back to local data on network error`() = runTest {
        // Arrange
        val localEntities = listOf(
            EventEntity("2", "Game", "event", "local_url")
        )
        val localEvents = localEntities.map { it.toDomain() }
        coEvery { remoteDataSource.getEvents() } throws IOException("Network error")
        coEvery { localDataSource.getEvents() } returns localEvents

        // Act
        val result = repository.fetchEvents()

        // Assert
        assertTrue(result is com.ticketmaster.domain.utils.Result.Success)
        assertEquals(localEvents, (result as com.ticketmaster.domain.utils.Result.Success).data)
        coVerify { localDataSource.getEvents() }
        coVerify(exactly = 0) { localDataSource.clearEvents() }
        coVerify(exactly = 0) { localDataSource.saveEvents(any()) }
    }

 @Test
    fun `fetchEvents falls back to local data on HTTP error`() = runTest {
        // Arrange
        val localEntities = listOf(
            EventEntity("3", "Game", "event", "local_url")
        )
        val localEvents = localEntities.map { it.toDomain() }
        coEvery { remoteDataSource.getEvents() } throws HttpException(mockk(relaxed = true))
        coEvery { localDataSource.getEvents() } returns localEvents

        // Act
        val result = repository.fetchEvents()

        // Assert
        assertTrue(result is com.ticketmaster.domain.utils.Result.Success)
        assertEquals(localEvents, (result as com.ticketmaster.domain.utils.Result.Success).data)
        coVerify { localDataSource.getEvents() }
        coVerify(exactly = 0) { localDataSource.clearEvents() }
        coVerify(exactly = 0) { localDataSource.saveEvents(any()) }
    }

    @Test
    fun `fetchEvents falls back to local data on timeout`() = runTest {
        // Arrange
        val localEntities = listOf(
            EventEntity("4", "Game", "event", "local_url")
        )
        val localEvents = localEntities.map { it.toDomain() }
        coEvery { remoteDataSource.getEvents() } coAnswers { delay(6000L); emptyList() }
        coEvery { localDataSource.getEvents() } returns localEvents

        // Act
        val result = repository.fetchEvents()

        // Assert
        assertTrue(result is com.ticketmaster.domain.utils.Result.Success)
        assertEquals(localEvents, (result as com.ticketmaster.domain.utils.Result.Success).data)
        coVerify { localDataSource.getEvents() }
        coVerify(exactly = 0) { localDataSource.clearEvents() }
        coVerify(exactly = 0) { localDataSource.saveEvents(any()) }
    }

    @Test
    fun `fetchEvents returns error when remote fails and no local data`() = runTest {
        // Arrange
        coEvery { remoteDataSource.getEvents() } throws IOException("No network")
        coEvery { localDataSource.getEvents() } returns emptyList()

        // Act
        val result = repository.fetchEvents()

        // Assert
        assertTrue(result is com.ticketmaster.domain.utils.Result.Error)
        assertEquals("No cached data available", (result as com.ticketmaster.domain.utils.Result.Error).exception.message)
        coVerify { localDataSource.getEvents() }
        coVerify(exactly = 0) { localDataSource.clearEvents() }
        coVerify(exactly = 0) { localDataSource.saveEvents(any()) }
    }

    @Test
    fun `fetchEvents returns error when both remote and local fail`() = runTest {
        // Arrange
        coEvery { remoteDataSource.getEvents() } throws IOException("No network")
        coEvery { localDataSource.getEvents() } throws Exception("Database error")

        // Act
        val result = repository.fetchEvents()

        // Assert
        assertTrue(result is com.ticketmaster.domain.utils.Result.Error)
        assertEquals("Database error", (result as com.ticketmaster.domain.utils.Result.Error).exception.message)
        coVerify { localDataSource.getEvents() }
        coVerify(exactly = 0) { localDataSource.clearEvents() }
        coVerify(exactly = 0) { localDataSource.saveEvents(any()) }
    }
}