package com.example.primebus.data.models

import kotlinx.serialization.Serializable

@Serializable
data class TripRequest(
    val from: String,
    val to: String,
    val journeyDate: Long?
)
