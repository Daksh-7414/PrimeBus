package com.example.primebus

import com.example.primebus.data.models.SeatUIModel

sealed interface SeatUiState {

    // Initial state while fetching layout + booked seats
    data object Loading : SeatUiState

    // Both layout and booked seats loaded — seat map ready to render
    // Holds List<SeatUIModel> (with AVAILABLE/BOOKED/LOCKED/SELECTED status)
    // Bug 1 fix: was List<SeatModel> — wrong type, causes compile error
    data class Success(
        val seatUIModels: List<SeatUIModel>
    ) : SeatUiState

    // Layout returned 0 seats after trimming to totalSeats
    // Bug 2 fix: was missing — used in ViewModel and Screen but never defined
    data object Empty : SeatUiState

    // Firebase error or unexpected exception
    data class Error(
        val message: String
    ) : SeatUiState

    // No network when screen opened
    data object NoInternet : SeatUiState

    // Not used in seat screen — kept for future booking confirmation flow
    data class BookingSuccess(
        val bookingId: String
    ) : SeatUiState
}