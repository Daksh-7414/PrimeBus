package com.example.primebus.data.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "bookings",
    foreignKeys = [
        ForeignKey(
            entity = Bus::class,
            parentColumns = ["busId"],
            childColumns = ["busId"],
            onDelete = ForeignKey.NO_ACTION
        )
    ],
    indices = [Index("busId")]
)
data class Booking(

    @PrimaryKey
    val bookingId: String = "",
    val userId: String = "",
    val busId: String = "",
    val busName: String = "",
    val routeId: String = "",

    // journeyDate as Long timestamp (midnight UTC of travel date)
    val journeyDate: Long = 0L,

    // "2026-06-10" - used for display and Firebase index queries
    val journeyDateStr: String = "",

    // "bus_101_2026-06-10" - Firebase indexed field for seat availability query
    val busId_date: String = "",

    val seats: List<String> = emptyList(),
    val passengers: List<Passenger> = emptyList(),
    val totalAmount: Double = 0.0,
    val status: String = "CONFIRMED",
    val timestamp: Long = 0L,
    val contactPhone: String = "",
    val contactEmail: String = "",
    val paymentId: String = ""
)