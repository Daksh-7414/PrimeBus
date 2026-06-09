package com.example.primebus

import com.example.primebus.data.models.BookedTripUiModel

sealed interface BookedTripsUiState {

    data object Loading : BookedTripsUiState

    data object Empty : BookedTripsUiState

    data object NoInternet : BookedTripsUiState

    data class Error(
        val message: String
    ) : BookedTripsUiState

    data class Success(
        val trips: List<BookedTripUiModel>,
        val isOffline: Boolean
    ) : BookedTripsUiState
}