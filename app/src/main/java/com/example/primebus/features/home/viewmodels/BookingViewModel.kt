package com.example.primebus.features.home.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.primebus.data.models.Booking
import com.example.primebus.data.models.Bus
import com.example.primebus.data.models.Passenger
import com.example.primebus.data.models.SeatModel
import com.example.primebus.data.models.TripRequest
import com.example.primebus.data.repository.BookingRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class BookingViewModel @Inject constructor(
    private val bookingRepository: BookingRepository,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _tripRequest = MutableStateFlow<TripRequest?>(null)
    val tripRequest = _tripRequest.asStateFlow()

    fun setTripRequest(tripRequest: TripRequest) {
        _tripRequest.value = tripRequest
    }

    val journeyRouteId: String
        get() {
            val req = _tripRequest.value ?: return ""
            return "${req.from.trim().lowercase()}_${req.to.trim().lowercase()}"
        }

    val journeyDateStr: String
        get() {
            val millis = _tripRequest.value?.journeyDate ?: return ""
            return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(millis))
        }

    // ── Bus selection ────────────────────────────────────────────
    private val _selectedBus = MutableStateFlow<Bus?>(null)
    val selectedBus = _selectedBus.asStateFlow()

    fun selectBus(bus: Bus) {
        _selectedBus.value = bus
    }

    private val _selectedSeats = MutableStateFlow<List<SeatModel>>(emptyList())
    val selectedSeats = _selectedSeats.asStateFlow()

    fun setSelectedSeats(seats: List<SeatModel>) {
        _selectedSeats.value = seats
    }

    private val _passengers = MutableStateFlow<List<Passenger>>(emptyList())
    val passengers = _passengers.asStateFlow()

    fun createPassengersFromSeats() {
        val seats = _selectedSeats.value
        val existing = _passengers.value
        _passengers.value = seats.map { seat ->
            existing.find { it.seatId == seat.seatId }
                ?: Passenger(seatId = seat.seatId, seatNumber = seat.seatNumber)
        }
    }

    fun updatePassenger(seatId: String, updated: Passenger) {
        val current = _passengers.value.toMutableList()
        val index   = current.indexOfFirst { it.seatId == seatId }
        if (index != -1) {
            current[index] = updated
            _passengers.value = current
        }
    }

    private val _contactPhone = MutableStateFlow("")
    val contactPhone = _contactPhone.asStateFlow()

    private val _contactEmail = MutableStateFlow("")
    val contactEmail = _contactEmail.asStateFlow()

    fun updateContactPhone(phone: String) { _contactPhone.value = phone }
    fun updateContactEmail(email: String) { _contactEmail.value = email }

    fun isBookingValid(): Boolean {
        val passengers = _passengers.value
        if (passengers.isEmpty()) return false
        return passengers.all {
            it.name.isNotBlank() && it.age.isNotBlank() && it.gender.isNotBlank()
        }
    }

    fun confirmBooking(
        paymentId: String,
        onSuccess: (bookingId: String) -> Unit,
        onFailure: (String) -> Unit
    ) {
        if (!isBookingValid()) {
            onFailure("Please fill all passenger details")
            return
        }

        val bus = _selectedBus.value
            ?: return onFailure("No bus selected")

        val uid = firebaseAuth.currentUser?.uid
            ?: return onFailure("Please log in to book")

        val trip = _tripRequest.value
            ?: return onFailure("Journey date missing")

        val dateStr = journeyDateStr
        if (dateStr.isEmpty()) return onFailure("Invalid journey date")
        Log.d("payment id in booking viewmodel","booking viewmodel ${paymentId}")
        val booking = Booking(
            userId = uid,
            busId = bus.busId,
            busName = bus.busName,
            routeId = bus.routeId,
            journeyDate = trip.journeyDate ?: 0L,
            journeyDateStr = dateStr,
            busId_date = "${bus.busId}_${dateStr}",
            seats = _selectedSeats.value.map { it.seatNumber },
            passengers = _passengers.value,
            totalAmount = _selectedSeats.value.size * bus.price,
            status = "CONFIRMED",
            timestamp = System.currentTimeMillis(),
            contactPhone = _contactPhone.value,
            contactEmail = _contactEmail.value,
            paymentId = paymentId
        )
        Log.d("payment id in booking viewmodel","booking viewmodel ${booking.paymentId}")
        Log.d("payment id in booking viewmodel","booking viewmodel ${booking}")

        viewModelScope.launch {
            val result = bookingRepository.confirmBooking(
                userId  = uid,
                booking = booking
            )
            result.onSuccess { bookingId ->
                clearBooking()
                onSuccess(bookingId)
            }
            result.onFailure { e ->
                onFailure(e.message ?: "Booking failed")
            }
        }
    }

    fun clearBooking() {
        _tripRequest.value   = null
        _selectedBus.value   = null
        _selectedSeats.value = emptyList()
        _passengers.value    = emptyList()
        _contactPhone.value  = ""
        _contactEmail.value  = ""
    }
}