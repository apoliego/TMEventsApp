package com.ticketmaster.data.network.model

import com.google.gson.annotations.SerializedName

data class Event(
    @SerializedName("name")
    val name: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("images")
    val images: List<Image>
) {
    fun toDomain(): com.ticketmaster.domain.model.Event {
        val selectedImageUrl = images.firstOrNull()?.url ?: ""
        return com.ticketmaster.domain.model.Event(
            id = id,
            name = name,
            type = type,
            imageUrl = selectedImageUrl
        )
    }
}

data class Events(
    @SerializedName("events")
    val events: List<Event>
) {
    fun toDomain(): List<com.ticketmaster.domain.model.Event> = events.map { it.toDomain() }
}

data class EventsResponse(
    @SerializedName("_embedded")
    val events: Events
) {
    fun toDomain(): List<com.ticketmaster.domain.model.Event> = events.toDomain()
}

data class Image(
    @SerializedName("ratio")
    val ratio: String,
    @SerializedName("url")
    val url: String,
)