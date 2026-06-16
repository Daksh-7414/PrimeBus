package com.example.primebus

import com.example.primebus.data.models.Bus

sealed interface BusUiState {

    data object Loading : BusUiState

    data object NoInternet : BusUiState

    // No buses exist for this route at all (raw data is empty)
    data object Empty : BusUiState

    // Raw data has buses, but none match the current filter
    data object NoFilterMatch : BusUiState

    data class Success(val buses: List<Bus>) : BusUiState

    data class Error(val message: String) : BusUiState
}