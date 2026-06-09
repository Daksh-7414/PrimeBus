package com.example.primebus

import com.example.primebus.data.models.Bus

sealed interface BusUiState {

    data object Loading : BusUiState

    data object NoInternet : BusUiState

    data object Empty : BusUiState

    data class Success(
        val buses: List<Bus>
    ) : BusUiState

    data class Error(
        val message: String
    ) : BusUiState
}