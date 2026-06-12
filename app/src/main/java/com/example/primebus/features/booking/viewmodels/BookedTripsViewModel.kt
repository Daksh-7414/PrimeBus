/*
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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookedTripsViewModel @Inject constructor(
    private val bookingRepository: BookingRepository,
    private val firebaseAuth: FirebaseAuth,
    private val networkMonitor: NetworkMonitor
) : ViewModel() {

    private val uid = firebaseAuth.currentUser?.uid.orEmpty()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    // 2. Add this refresh() function:
    fun refresh() {
        if (_isRefreshing.value) return   // already refreshing — ignore duplicate

        val userId = firebaseAuth.currentUser?.uid ?: return
        _isRefreshing.value = true

        viewModelScope.launch {
            // Re-run your sync — same as what happens on first load
            bookingRepository.getUserBookings(
                userId,
                networkMonitor.isOnline.value
            )
            _isRefreshing.value = false
        }
    }

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
 */
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
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookedTripsViewModel @Inject constructor(
    private val bookingRepository: BookingRepository,
    private val firebaseAuth: FirebaseAuth,
    private val networkMonitor: NetworkMonitor
) : ViewModel() {

    private val uid = firebaseAuth.currentUser?.uid.orEmpty()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    // Refresh trigger – increments each time user pulls to refresh
    private val refreshTrigger = MutableStateFlow(0)

    val uiState: StateFlow<BookedTripsUiState> =

        combine(
            networkMonitor.isOnline,
            refreshTrigger
        ) { online, _ -> online }
            .flatMapLatest { online ->
                // This will re-run every time isOnline changes OR refreshTrigger emits
                bookingRepository.getUserBookings(
                    userId = uid,
                    isOnline = online
                )
            }
            .map { result ->

                when (result) {
                    AppResult.Loading -> {
                        // If it's a manual refresh, we might want to keep previous state,
                        // but here we treat loading as usual.
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

    // Refresh function – called when user pulls to refresh
    fun refresh() {
        // Prevent multiple concurrent refreshes
        if (_isRefreshing.value) return

        val userId = firebaseAuth.currentUser?.uid ?: return

        _isRefreshing.value = true

        viewModelScope.launch {
            try {
                // Increment trigger to force flatMapLatest to re-execute
                refreshTrigger.value++

                // Optional: you can also directly call repository to fetch fresh data,
                // but the flow above will already re-collect because of refreshTrigger.
                // However, if getUserBookings() caches results, you may need to explicitly
                // clear cache here. For now, incrementing trigger is enough.
            } finally {
                _isRefreshing.value = false
            }
        }
    }

    private val _selectedTrip = MutableStateFlow<BookedTripUiModel?>(null)
    val selectedTrip = _selectedTrip.asStateFlow()

    fun selectTrip(trip: BookedTripUiModel) {
        _selectedTrip.value = trip
    }

    fun clearSelectedTrip() {
        _selectedTrip.value = null
    }
}