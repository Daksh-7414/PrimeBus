package com.example.primebus

import com.example.primebus.data.models.SeatUIModel

sealed interface SeatUiState {

    data object Loading : SeatUiState

    data class Success(val seatUIModels: List<SeatUIModel>) : SeatUiState

    data object Empty : SeatUiState

    data class Error(val message: String) : SeatUiState

    data object NoInternet : SeatUiState

    data class BookingSuccess(val bookingId: String) : SeatUiState
}