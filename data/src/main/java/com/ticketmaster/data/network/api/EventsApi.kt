package com.ticketmaster.data.network.api

import com.ticketmaster.data.network.model.EventsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface EventsApi {

    @GET("events")
    suspend fun fetchEvents(
        @Query("apikey") apiKey: String,
    ): EventsResponse

    companion object {
        const val API_BASE_URL = "https://app.ticketmaster.com/discovery/v2/"
        const val API_KEY = "DW0E98NrxUIfDDtNN7ijruVSm60ryFLX"
    }
}