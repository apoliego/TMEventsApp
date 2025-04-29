package com.ticketmaster.data.repository

import com.ticketmaster.data.repository.datasource.LocalEventDataSource
import com.ticketmaster.data.repository.datasource.RemoteEventDataSource
import com.ticketmaster.domain.model.Event
import com.ticketmaster.domain.repository.EventsRepository
import com.ticketmaster.domain.utils.Result
import kotlinx.coroutines.withTimeoutOrNull
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class EventsRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteEventDataSource,
    private val localDataSource: LocalEventDataSource
) : EventsRepository {
    override suspend fun fetchEvents(): Result<List<Event>> {
        // Try fetching from remote with a timeout of 5 seconds
        val remoteResult = withTimeoutOrNull(5000L) {
            try {
                val remoteEvents = remoteDataSource.getEvents()
                // Cache remote events in local database
                localDataSource.clearEvents()
                localDataSource.saveEvents(remoteEvents)
                return@withTimeoutOrNull Result.Success(remoteEvents)
            } catch (e: IOException) {
                // Network error
                return@withTimeoutOrNull null
            } catch (e: HttpException) {
                // HTTP error (e.g., 404, 500)
                return@withTimeoutOrNull null
            }
        }

        // If remote fetch succeeded, return the result
        if (remoteResult is Result.Success) {
            return remoteResult
        }

        // Fallback to local data
        return try {
            val localEvents = localDataSource.getEvents()
            if (localEvents.isNotEmpty()) {
                Result.Success(localEvents)
            } else {
                Result.Error(Exception("No cached data available"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}