package com.example.primebus.features.home.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.primebus.AppResult
import com.example.primebus.SeatUiState
import com.example.primebus.data.models.SeatModel
import com.example.primebus.data.models.SeatStatus
import com.example.primebus.data.models.SeatUIModel
import com.example.primebus.data.repository.SeatRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SeatViewModel @Inject constructor(
    private val seatRepository: SeatRepository,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    companion object {
        private const val TAG = "SeatViewModel"
    }

    private val _uiState = MutableStateFlow<SeatUiState>(SeatUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _layout = MutableStateFlow<List<SeatModel>>(emptyList())
    private val _bookedSeats = MutableStateFlow<Set<String>>(emptySet())
    private val _lockedByOthers = MutableStateFlow<Set<String>>(emptySet())
    private val _selectedSeats = MutableStateFlow<Set<String>>(emptySet())
    val selectedSeats = _selectedSeats.asStateFlow()

    private val lockTimerJobs = mutableMapOf<String, Job>()

    private var currentBusId   = ""
    private var currentDateStr = ""

    fun loadSeats(
        busId: String,
        busType: String,
        journeyDateStr: String,
        totalSeats: Int
    ) {
        currentBusId = busId
        currentDateStr = journeyDateStr

        _uiState.value = SeatUiState.Loading
        _selectedSeats.value = emptySet()
        _layout.value = emptyList()
        _bookedSeats.value = emptySet()
        _lockedByOthers.value = emptySet()

        viewModelScope.launch {

            // Step 1: seat layout
            val layoutResult = seatRepository.getSeatLayout(busType)
            val finalLayout = when (layoutResult) {
                is AppResult.NoInternet -> {
                    _uiState.value = SeatUiState.NoInternet
                    return@launch
                }
                is AppResult.Error -> {
                    _uiState.value = SeatUiState.Error(layoutResult.message)
                    return@launch
                }
                is AppResult.Success -> {
                    val raw = if (layoutResult.data.isNotEmpty()) {
                        layoutResult.data
                    } else {
                        generateDynamicSeats(busType, totalSeats)
                    }
                    val trimmed = if (totalSeats > 0 && raw.size > totalSeats) raw.take(totalSeats) else raw
                    if (trimmed.isEmpty()) {
                        _uiState.value = SeatUiState.Empty
                        return@launch
                    }
                    trimmed
                }
                is AppResult.Loading -> return@launch
            }
            _layout.value = finalLayout

            // Step 2: booked seats
            when (val bookedResult = seatRepository.getBookedSeatNumbers(busId, journeyDateStr)) {
                is AppResult.NoInternet -> {
                    _uiState.value = SeatUiState.NoInternet
                    return@launch
                }
                is AppResult.Error -> {
                    _uiState.value = SeatUiState.Error(bookedResult.message)
                    return@launch
                }
                is AppResult.Success -> {
                    _bookedSeats.value = bookedResult.data
                    // ── NEW: all seats already booked? ──
                    if (finalLayout.size == bookedResult.data.size) {
                        _uiState.value = SeatUiState.Empty
                        return@launch
                    }
                }
                is AppResult.Loading -> return@launch
            }

            // Step 3 & 4: real-time locks and recomposition
            recomputeAndEmit()
            val userId = firebaseAuth.currentUser?.uid ?: ""

            launch {
                seatRepository.observeLockedSeats(busId, journeyDateStr, userId)
                    .collect { locked ->
                        _lockedByOthers.value = locked
                        recomputeAndEmit()
                    }
            }

            launch {
                combine(_layout, _bookedSeats, _lockedByOthers, _selectedSeats) { _, _, _, _ -> }
                    .collect { recomputeAndEmit() }
            }
        }
    }

    private fun recomputeAndEmit() {
        val layout   = _layout.value
        val booked   = _bookedSeats.value

        if (layout.isNotEmpty() && layout.size == booked.size) {
            _uiState.value = SeatUiState.Empty
            return
        }

        val locked   = _lockedByOthers.value
        val selected = _selectedSeats.value

        val uiModels = layout.map { seat ->
            SeatUIModel(
                seat   = seat,
                status = when {
                    selected.contains(seat.seatNumber) -> SeatStatus.SELECTED
                    booked.contains(seat.seatNumber)   -> SeatStatus.BOOKED
                    locked.contains(seat.seatNumber)   -> SeatStatus.LOCKED
                    else                               -> SeatStatus.AVAILABLE
                }
            )
        }

        val current = _uiState.value
        if (current !is SeatUiState.Error && current != SeatUiState.NoInternet) {
            _uiState.value = SeatUiState.Success(uiModels)
        }
    }

    fun onSeatClick(seatNumber: String) {
        val current = _selectedSeats.value

        if (current.contains(seatNumber)) {
            _selectedSeats.value = current - seatNumber
            cancelLockTimer(seatNumber)
            viewModelScope.launch {
                seatRepository.releaseSeatLock(currentBusId, currentDateStr, seatNumber)
            }
        } else {
            if (_bookedSeats.value.contains(seatNumber) ||
                _lockedByOthers.value.contains(seatNumber)) return

            viewModelScope.launch {
                val userId = firebaseAuth.currentUser?.uid ?: return@launch
                seatRepository.lockSeat(currentBusId, currentDateStr, seatNumber, userId)
                    .onSuccess {
                        _selectedSeats.value = _selectedSeats.value + seatNumber
                        startLockTimer(seatNumber)
                        Log.d(TAG, "Seat $seatNumber locked")
                    }
                    .onFailure {
                        Log.e(TAG, "Lock failed for $seatNumber: ${it.message}")
                        showErrorThenRestore("Seat $seatNumber is no longer available")
                    }
            }
        }
    }

    private fun startLockTimer(seatNumber: String) {
        lockTimerJobs[seatNumber]?.cancel()
        lockTimerJobs[seatNumber] = viewModelScope.launch {
            delay(SeatRepository.LOCK_DURATION_MS)
            if (_selectedSeats.value.contains(seatNumber)) {
                _selectedSeats.value = _selectedSeats.value - seatNumber
                seatRepository.releaseSeatLock(currentBusId, currentDateStr, seatNumber)
                Log.w(TAG, "Lock expired for $seatNumber")
                showErrorThenRestore("Seat $seatNumber lock expired. Please reselect.")
            }
            lockTimerJobs.remove(seatNumber)
        }
    }

    private fun showErrorThenRestore(message: String) {
        _uiState.value = SeatUiState.Error(message)
        viewModelScope.launch {
            delay(3000)
            if (_uiState.value is SeatUiState.Error) {
                recomputeAndEmit()
            }
        }
    }

    private fun cancelLockTimer(seatNumber: String) {
        lockTimerJobs[seatNumber]?.cancel()
        lockTimerJobs.remove(seatNumber)
    }

    fun releaseAllSelectedSeats() {
        val toRelease = _selectedSeats.value.toList()
        if (toRelease.isEmpty()) return
        toRelease.forEach { cancelLockTimer(it) }
        _selectedSeats.value = emptySet()
        viewModelScope.launch {
            seatRepository.releaseAllLocks(currentBusId, currentDateStr, toRelease)
        }
    }

    // clearError — called from screen after error auto-dismiss
    fun clearError() {
        if (_uiState.value is SeatUiState.Error || _uiState.value == SeatUiState.NoInternet) {
            if (_layout.value.isNotEmpty()) recomputeAndEmit()
            else _uiState.value = SeatUiState.Loading
        }
    }

    private fun generateDynamicSeats(busType: String, totalSeats: Int): List<SeatModel> {
        if (totalSeats <= 0) return emptyList()
        val isSleeper = busType.contains("Sleeper", ignoreCase = true) &&
                !busType.contains("Non", ignoreCase = true)

        return if (isSleeper) {
            val rows = (totalSeats + 1) / 2
            (0 until rows).flatMap { row ->
                listOf(
                    SeatModel(seatId = "L${row+1}", seatNumber = "L${row+1}", row = row, column = 0, side = "L"),
                    SeatModel(seatId = "R${row+1}", seatNumber = "R${row+1}", row = row, column = 1, side = "R")
                )
            }.take(totalSeats)
        } else {
            val rowLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
            val rows = (totalSeats + 3) / 4
            (0 until rows).flatMap { row ->
                val p = rowLetters[row].toString()
                listOf(
                    SeatModel("${p}1", "${p}1", row, 0),
                    SeatModel("${p}2", "${p}2", row, 1),
                    SeatModel("${p}3", "${p}3", row, 2),
                    SeatModel("${p}4", "${p}4", row, 3)
                )
            }.take(totalSeats)
        }
    }

    override fun onCleared() {
        super.onCleared()
        lockTimerJobs.values.forEach { it.cancel() }
        lockTimerJobs.clear()
    }
}