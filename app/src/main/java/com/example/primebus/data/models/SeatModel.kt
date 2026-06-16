package com.example.primebus.data.models

data class SeatModel(
    val seatId: String = "",
    val seatNumber: String = "",
    val row: Int = 0,
    val column: Int = 0,
    val side: String = ""
)

enum class SeatStatus { AVAILABLE, BOOKED, LOCKED, SELECTED }

data class SeatUIModel(
    val seat: SeatModel,
    val status: SeatStatus = SeatStatus.AVAILABLE
)