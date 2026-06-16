package com.example.primebus.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "buses")
data class Bus(
    @PrimaryKey
    val busId: String = "",
    val busName: String = "",
    val type: String = "AC Sleeper",
    val rating: Double = 4.2,
    val price: Double = 0.0,
    val departureTime: String = "",
    val arrivalTime: String = "",
    val source: String = "",
    val destination: String = "",
    val duration: String = "",
    val totalSeats: Int = 10,
    val boardingPoint: String = "",
    val droppingPoint: String = "",
    val routeId: String = "",
    val operator: String = "",
    val isActive: Boolean = true
)