package com.example.primebus.data.models

// Layout-only model — no status/lock fields here anymore.
// Status is computed at runtime from bookings + seatLocks.
data class SeatModel(
    val seatId: String = "",        // e.g. "L1", "A3"
    val seatNumber: String = "",    // same as seatId for display
    val row: Int = 0,
    val column: Int = 0,
    val side: String = ""           // "L" or "R" for sleeper, "" for non-sleeper
)

// Runtime UI state — computed, never stored in Firebase
enum class SeatStatus { AVAILABLE, BOOKED, LOCKED, SELECTED }

data class SeatUIModel(
    val seat: SeatModel,
    val status: SeatStatus = SeatStatus.AVAILABLE
)