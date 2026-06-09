package com.example.primebus.features.booking.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.primebus.AppResult
import com.example.primebus.BookedTripsUiState
import com.example.primebus.NetworkMonitor
import com.example.primebus.data.models.BookedTripUiModel
import com.example.primebus.data.repository.BookingRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class BookedTripsViewModel @Inject constructor(
    private val bookingRepository: BookingRepository,
    private val firebaseAuth: FirebaseAuth,
    private val networkMonitor: NetworkMonitor
) : ViewModel() {

    private val uid = firebaseAuth.currentUser?.uid.orEmpty()

    val uiState: StateFlow<BookedTripsUiState> =

        networkMonitor.isOnline
            .flatMapLatest { online ->

                bookingRepository.getUserBookings(
                    userId = uid,
                    isOnline = online
                )
            }
            .map { result ->

                when (result) {

                    AppResult.Loading -> {
                        BookedTripsUiState.Loading
                    }

                    AppResult.NoInternet -> {
                        BookedTripsUiState.NoInternet
                    }

                    is AppResult.Error -> {
                        BookedTripsUiState.Error(result.message)
                    }

                    is AppResult.Success -> {

                        if (result.data.isEmpty()) {
                            BookedTripsUiState.Empty
                        } else {
                            BookedTripsUiState.Success(
                                trips = result.data,
                                isOffline = !networkMonitor.isOnline.value
                            )
                        }
                    }
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = BookedTripsUiState.Loading
            )

    private val _selectedTrip =
        MutableStateFlow<BookedTripUiModel?>(null)

    val selectedTrip = _selectedTrip.asStateFlow()

    fun selectTrip(trip: BookedTripUiModel) {
        _selectedTrip.value = trip
    }

    fun clearSelectedTrip() {
        _selectedTrip.value = null
    }
}