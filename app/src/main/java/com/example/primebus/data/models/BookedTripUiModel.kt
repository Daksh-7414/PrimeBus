package com.example.primebus.data.models

import androidx.room.Embedded
import androidx.room.Relation

data class BookedTripUiModel(
    @Embedded
    val booking: Booking,
    @Relation(
        parentColumn = "busId",
        entityColumn = "busId"
    )
    val bus: Bus
)