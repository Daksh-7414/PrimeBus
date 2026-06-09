/*
package com.example.primebus.features.home.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.primebus.AppResult
import com.example.primebus.SeatUiState
import com.example.primebus.data.models.SeatModel
import com.example.primebus.data.models.SeatUIModel
import com.example.primebus.data.models.SeatStatus
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

    // ── Raw layout from DB (set once per busType)

    private val _uiState = MutableStateFlow<SeatUiState>(SeatUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _layout = MutableStateFlow<List<SeatModel>>(emptyList())
    private val _bookedSeats = MutableStateFlow<Set<String>>(emptySet())
    private val _lockedByOthers = MutableStateFlow<Set<String>>(emptySet())
    private val _selectedSeats = MutableStateFlow<Set<String>>(emptySet())
    val selectedSeats = _selectedSeats.asStateFlow()

    val seatUIModels = MutableStateFlow<List<SeatUIModel>>(emptyList())

    // ── Loading state ──────────────────────────────────────────────
//    private val _isLoading = MutableStateFlow(true)
//    val isLoading = _isLoading.asStateFlow()

    // ── Error state ────────────────────────────────────────────────
//    private val _error = MutableStateFlow<String?>(null)
//    val error = _error.asStateFlow()

    // ── Lock expiry timers ─────────────────────────────────────────
    private val lockTimerJobs = mutableMapOf<String, Job>()

    // ── Context stored for release on dispose ─────────────────────
    private var currentBusId = ""
    private var currentDateStr = ""

    // ─────────────────────────────────────────────────────────────
    // Called from BusSeatScreen LaunchedEffect
    // Loads layout template + booked seats + starts lock listener
    // ─────────────────────────────────────────────────────────────
    /*
    fun loadSeats(busId: String, busType: String, journeyDateStr: String) {
        currentBusId   = busId
        currentDateStr = journeyDateStr

        _uiState.value     = SeatUiState.Loading
        _selectedSeats.value = emptySet()

        viewModelScope.launch {

            // ── Step 1: load seat layout template ─────────────────────────────

            when (val layoutResult = seatRepository.getSeatLayout(busType)) {

                is AppResult.NoInternet -> {
                    Log.w("TAG", "No internet while loading layout")
                    _uiState.value = SeatUiState.NoInternet
                    return@launch
                }

                is AppResult.Error -> {
                    Log.e("TAG", "Layout load error: ${layoutResult.message}")
                    _uiState.value = SeatUiState.Error(layoutResult.message)
                    return@launch
                }

                is AppResult.Success -> {
                    if (layoutResult.data.isEmpty()) {
                        _uiState.value = SeatUiState.Error("No seat layout found for $busType")
                        return@launch
                    }
                    _layout.value = layoutResult.data
                    Log.d("TAG", "Layout loaded: ${layoutResult.data.size} seats")
                }

                is AppResult.Loading -> { /* handled by initial state */ }
            }

            // ── Step 2: load booked seats for this date ────────────────────────

            when (val bookedResult = seatRepository.getBookedSeatNumbers(busId, journeyDateStr)) {

                is AppResult.NoInternet -> {
                    Log.w("TAG", "No internet while loading booked seats")
                    _uiState.value = SeatUiState.NoInternet
                    return@launch
                }

                is AppResult.Error -> {
                    Log.e("TAG", "Booked seats error: ${bookedResult.message}")
                    _uiState.value = SeatUiState.Error(bookedResult.message)
                    return@launch
                }

                is AppResult.Success -> {
                    _bookedSeats.value = bookedResult.data
                    Log.d("TAG", "Booked seats loaded: ${bookedResult.data.size} seats")
                }

                is AppResult.Loading -> { /* handled by initial state */ }
            }

            // Both layout and booked seats loaded — show the seat map
            recomputeUI()
            _uiState.value = SeatUiState.Success(_layout.value)

            // ── Step 3: real-time lock listener for other users ────────────────
            val userId = firebaseAuth.currentUser?.uid ?: ""
            launch {
                seatRepository.observeLockedSeats(busId, journeyDateStr, userId)
                    .collect { locked ->
                        _lockedByOthers.value = locked
                        recomputeUI()
                        // Update Success state with fresh seat list
                        if (_uiState.value is SeatUiState.Success) {
                            _uiState.value = SeatUiState.Success(_layout.value)
                        }
                    }
            }

            // ── Step 4: recompute UI whenever selected seats change ────────────
            launch {
                combine(_layout, _bookedSeats, _lockedByOthers, _selectedSeats) {
                        _, _, _, _ -> Unit
                }.collect {
                    recomputeUI()
                    // Keep Success state updated with latest seat models
                    if (_uiState.value is SeatUiState.Success) {
                        _uiState.value = SeatUiState.Success(_layout.value)
                    }
                }
            }
        }
    }
    // ─────────────────────────────────────────────────────────────
    // Recompute UI model list from the 4 independent state sources
    // Priority: SELECTED > BOOKED > LOCKED > AVAILABLE
    // ─────────────────────────────────────────────────────────────
    private fun recomputeUI() {
        val layout   = _layout.value
        val booked   = _bookedSeats.value
        val locked   = _lockedByOthers.value
        val selected = _selectedSeats.value

        seatUIModels.value = layout.map { seat ->
            val status = when {
                selected.contains(seat.seatNumber) -> SeatStatus.SELECTED
                booked.contains(seat.seatNumber)   -> SeatStatus.BOOKED
                locked.contains(seat.seatNumber)   -> SeatStatus.LOCKED
                else                               -> SeatStatus.AVAILABLE
            }
            SeatUIModel(seat = seat, status = status)
        }
    }

    // ─────────────────────────────────────────────────────────────
    // Called when user taps a seat
    // ─────────────────────────────────────────────────────────────
    fun onSeatClick(seatNumber: String) {
        val current = _selectedSeats.value

        if (current.contains(seatNumber)) {
            // Deselect: remove from selected, cancel timer, release lock
            _selectedSeats.value = current - seatNumber
            cancelLockTimer(seatNumber)
            viewModelScope.launch {
                seatRepository.releaseSeatLock(currentBusId, currentDateStr, seatNumber)
            }
        } else {
            // Select: check it's still available, lock it, start timer
            val isBooked = _bookedSeats.value.contains(seatNumber)
            val isLocked = _lockedByOthers.value.contains(seatNumber)
            if (isBooked || isLocked) return  // already taken, ignore tap

            viewModelScope.launch {
                val userId = firebaseAuth.currentUser?.uid ?: return@launch
                val result = seatRepository.lockSeat(
                    busId          = currentBusId,
                    journeyDateStr = currentDateStr,
                    seatNumber     = seatNumber,
                    userId         = userId
                )
                result.onSuccess {
                    _selectedSeats.value += seatNumber
                    startLockTimer(seatNumber)
                }
                result.onFailure {
                    _uiState.value = SeatUiState.Error("Seat $seatNumber is no longer available")
                    // Auto‑clear after 3 seconds (optional)
                    viewModelScope.launch {
                        delay(3000)
                        if (_uiState.value is SeatUiState.Error) {
                            _uiState.value = SeatUiState.Success(_layout.value)
                        }
                    }
                }
            }
        }
    }

    // ─────────────────────────────────────────────────────────────
    // Lock timer — auto-release seat after LOCK_DURATION if not booked
    // Timer and Firebase lock duration are BOTH 10 minutes (Bug 7 fix)
    // ─────────────────────────────────────────────────────────────
    private fun startLockTimer(seatNumber: String) {
        lockTimerJobs[seatNumber]?.cancel()
        lockTimerJobs[seatNumber] = viewModelScope.launch {
            delay(SeatRepository.LOCK_DURATION_MS)
            if (_selectedSeats.value.contains(seatNumber)) {
                _selectedSeats.value = _selectedSeats.value - seatNumber
                seatRepository.releaseSeatLock(currentBusId, currentDateStr, seatNumber)
                _uiState.value = SeatUiState.Error("Seat $seatNumber lock expired. Please reselect.")
                viewModelScope.launch {
                    delay(3000)
                    if (_uiState.value is SeatUiState.Error) {
                        _uiState.value = SeatUiState.Success(_layout.value)
                    }
                }
            }
            lockTimerJobs.remove(seatNumber)
        }
    }

    private fun cancelLockTimer(seatNumber: String) {
        lockTimerJobs[seatNumber]?.cancel()
        lockTimerJobs.remove(seatNumber)
    }

    // ─────────────────────────────────────────────────────────────
    // Release ALL selected seats — call on back press / screen dispose
    // ─────────────────────────────────────────────────────────────
    fun releaseAllSelectedSeats() {
        val toRelease = _selectedSeats.value.toList()
        if (toRelease.isEmpty()) return
        toRelease.forEach { cancelLockTimer(it) }
        _selectedSeats.value = emptySet()
        viewModelScope.launch {
            seatRepository.releaseAllLocks(currentBusId, currentDateStr, toRelease)
        }
    }

    // Clear error – reset to Success if layout is loaded, else Loading
    fun clearError() {
        when (_uiState.value) {
            is SeatUiState.Error, SeatUiState.NoInternet -> {
                if (_layout.value.isNotEmpty()) {
                    _uiState.value = SeatUiState.Success(_layout.value)
                } else {
                    _uiState.value = SeatUiState.Loading
                }
            }
            else -> { /* ignore */ }
        }
    }

    override fun onCleared() {
        super.onCleared()
        lockTimerJobs.values.forEach { it.cancel() }
        lockTimerJobs.clear()
    }

     */

    fun loadSeats(busId: String, busType: String, journeyDateStr: String,totalSeats: Int) {
        currentBusId = busId
        currentDateStr = journeyDateStr

        _uiState.value = SeatUiState.Loading
        _selectedSeats.value = emptySet()

        viewModelScope.launch {

            // Step 1: load seat layout template
            val layoutResult = seatRepository.getSeatLayout(busType)
            when (layoutResult) {
                is AppResult.NoInternet -> {
                    _uiState.value = SeatUiState.NoInternet
                    return@launch
                }
                is AppResult.Error -> {
                    _uiState.value = SeatUiState.Error(layoutResult.message)
                    return@launch
                }
                is AppResult.Success -> {
                    val layoutList = layoutResult.data.ifEmpty {
                        Log.w("TAG", "No layout for $busType, generating $totalSeats dynamic seats")
                        generateDynamicSeats(busType, totalSeats)
                    }
                    if (layoutList.isEmpty()) {
                        _uiState.value = SeatUiState.Empty
                        return@launch
                    }
                    val trimmed = if (totalSeats > 0 && layoutList.size > totalSeats) {
                        layoutList.take(totalSeats)
                    } else {
                        layoutList
                    }
                    if (trimmed.isEmpty()) {
                        _uiState.value = SeatUiState.Empty
                        return@launch
                    }
                    _layout.value = trimmed
                    Log.d("SeatVM", "Layout set with ${trimmed.size} seats")
                }
                is AppResult.Loading -> { /* no-op */ }   // ✅ 'is' added
            }
            // Step 2: load booked seat numbers
            val bookedResult = seatRepository.getBookedSeatNumbers(busId, journeyDateStr)
            when (bookedResult) {
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
                }

                AppResult.Loading -> { /* no-op */ }
            }

            // Both loaded successfully -> compute initial UI state
            updateUiState()

            // Step 3: real-time lock listener (other users)
            val userId = firebaseAuth.currentUser?.uid ?: ""
            launch {
                seatRepository.observeLockedSeats(busId, journeyDateStr, userId)
                    .collect { locked ->
                        _lockedByOthers.value = locked
                        updateUiState()
                        _uiState.value = SeatUiState.Success(seatUIModels.value)
                    }
            }

            // Step 4: recompute UI whenever selected seats change
            launch {
                combine(_layout, _bookedSeats, _lockedByOthers, _selectedSeats) { _, _, _, _ -> }
                    .collect {
                        updateUiState()
                    }
            }
        }
    }

    private fun generateDynamicSeats(busType: String, totalSeats: Int): List<SeatModel> {
        val isSleeper = busType.contains("Sleeper", ignoreCase = true) &&
                !busType.contains("Non", ignoreCase = true)
        return if (isSleeper) {
            val rows = (totalSeats + 1) / 2
            (0 until rows).flatMap { row ->
                listOf(
                    SeatModel(seatNumber = "L${row + 1}", seatId = "L${row + 1}", row = row, column = 0),
                    SeatModel(seatNumber = "R${row + 1}", seatId = "R${row + 1}", row = row, column = 1)
                )
            }.take(totalSeats)
        } else {
            val rows = (totalSeats + 3) / 4
            (0 until rows).flatMap { row ->
                val prefix = ('A' + row).toString()
                listOf(
                    SeatModel("${prefix}1", "${prefix}1", row, 0),
                    SeatModel("${prefix}2", "${prefix}2", row, 1),
                    SeatModel("${prefix}3", "${prefix}3", row, 2),
                    SeatModel("${prefix}4", "${prefix}4", row, 3)
                )
            }.take(totalSeats)
        }
    }    // ─────────────────────────────────────────────────────────────
    // Recompute UI models from current internal state and emit new Success
    // ─────────────────────────────────────────────────────────────
    private fun updateUiState() {
        val layout = _layout.value
        val booked = _bookedSeats.value
        val locked = _lockedByOthers.value
        val selected = _selectedSeats.value

        val uiModels = layout.map { seat ->
            val status = when {
                selected.contains(seat.seatNumber) -> SeatStatus.SELECTED
                booked.contains(seat.seatNumber)   -> SeatStatus.BOOKED
                locked.contains(seat.seatNumber)   -> SeatStatus.LOCKED
                else                               -> SeatStatus.AVAILABLE
            }
            SeatUIModel(seat = seat, status = status)
        }

        // Only emit Success if we are not in an error state
        if (_uiState.value !is SeatUiState.Error && _uiState.value != SeatUiState.NoInternet) {
            _uiState.value = SeatUiState.Success(uiModels)
        }
    }

    // ─────────────────────────────────────────────────────────────
    // User interaction: tap on a seat
    // ─────────────────────────────────────────────────────────────
    fun onSeatClick(seatNumber: String) {
        val current = _selectedSeats.value

        if (current.contains(seatNumber)) {
            // Deselect
            _selectedSeats.value = current - seatNumber
            cancelLockTimer(seatNumber)
            viewModelScope.launch {
                seatRepository.releaseSeatLock(currentBusId, currentDateStr, seatNumber)
            }
        } else {
            // Select: verify still available
            val isBooked = _bookedSeats.value.contains(seatNumber)
            val isLocked = _lockedByOthers.value.contains(seatNumber)
            if (isBooked || isLocked) return

            viewModelScope.launch {
                val userId = firebaseAuth.currentUser?.uid ?: return@launch
                val result = seatRepository.lockSeat(
                    busId = currentBusId,
                    journeyDateStr = currentDateStr,
                    seatNumber = seatNumber,
                    userId = userId
                )
                result.onSuccess {
                    _selectedSeats.value += seatNumber
                    startLockTimer(seatNumber)
                }
                result.onFailure {
                    _uiState.value = SeatUiState.Error("Seat $seatNumber is no longer available")
                    viewModelScope.launch {
                        delay(3000)
                        if (_uiState.value is SeatUiState.Error) {
                            // revert to previous success state if layout still valid
                            if (_layout.value.isNotEmpty()) {
                                updateUiState()
                            } else {
                                _uiState.value = SeatUiState.Loading
                            }
                        }
                    }
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
                _uiState.value = SeatUiState.Error("Seat $seatNumber lock expired. Please reselect.")
                viewModelScope.launch {
                    delay(3000)
                    if (_uiState.value is SeatUiState.Error && _layout.value.isNotEmpty()) {
                        updateUiState()
                    }
                }
            }
            lockTimerJobs.remove(seatNumber)
        }
    }

    private fun cancelLockTimer(seatNumber: String) {
        lockTimerJobs[seatNumber]?.cancel()
        lockTimerJobs.remove(seatNumber)
    }

    // ─────────────────────────────────────────────────────────────
    // Release all locks when screen is destroyed
    // ─────────────────────────────────────────────────────────────
    fun releaseAllSelectedSeats() {
        val toRelease = _selectedSeats.value.toList()
        if (toRelease.isEmpty()) return
        toRelease.forEach { cancelLockTimer(it) }
        _selectedSeats.value = emptySet()
        viewModelScope.launch {
            seatRepository.releaseAllLocks(currentBusId, currentDateStr, toRelease)
        }
    }

    // ─────────────────────────────────────────────────────────────
    // Clear error (used after auto-dismiss timer)
    // ─────────────────────────────────────────────────────────────
    fun clearError() {
        if (_uiState.value is SeatUiState.Error || _uiState.value == SeatUiState.NoInternet) {
            if (_layout.value.isNotEmpty()) {
                updateUiState()
            } else {
                _uiState.value = SeatUiState.Loading
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        lockTimerJobs.values.forEach { it.cancel() }
        lockTimerJobs.clear()
    }
}
 */
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

    // ── Single state the screen observes ──────────────────────────
    private val _uiState = MutableStateFlow<SeatUiState>(SeatUiState.Loading)
    val uiState = _uiState.asStateFlow()

    // ── Internal sources — combined to produce SeatUiState.Success ─
    private val _layout         = MutableStateFlow<List<SeatModel>>(emptyList())
    private val _bookedSeats    = MutableStateFlow<Set<String>>(emptySet())
    private val _lockedByOthers = MutableStateFlow<Set<String>>(emptySet())
    private val _selectedSeats  = MutableStateFlow<Set<String>>(emptySet())
    val selectedSeats = _selectedSeats.asStateFlow()

    // ── Lock timers ────────────────────────────────────────────────
    private val lockTimerJobs = mutableMapOf<String, Job>()

    // ── Current context for lock/release calls ─────────────────────
    private var currentBusId   = ""
    private var currentDateStr = ""

    // ─────────────────────────────────────────────────────────────
    // loadSeats — entry point from BusSeatScreen
    // totalSeats: trims the template layout to the bus's actual count
    // ─────────────────────────────────────────────────────────────
//    fun loadSeats(
//        busId: String,
//        busType: String,
//        journeyDateStr: String,
//        totalSeats: Int
//    ) {
//        currentBusId   = busId
//        currentDateStr = journeyDateStr
//
//        _uiState.value       = SeatUiState.Loading
//        _selectedSeats.value = emptySet()
//        _layout.value        = emptyList()
//        _bookedSeats.value   = emptySet()
//        _lockedByOthers.value = emptySet()
//
//        viewModelScope.launch {
//
//            // ── Step 1: seat layout ──────────────────────────────────────
//            val layoutResult = seatRepository.getSeatLayout(busType)
//            val finalLayout = when (layoutResult) {
//                is AppResult.NoInternet -> {
//                    _uiState.value = SeatUiState.NoInternet
//                    return@launch
//                }
//                is AppResult.Error -> {
//                    _uiState.value = SeatUiState.Error(layoutResult.message)
//                    return@launch
//                }
//                is AppResult.Success -> {
//                    // Firebase returned seats from seatLayouts/{busType}/seats
//                    // If empty (e.g. new bus type not in DB yet), generate dynamically
//                    val raw = if (layoutResult.data.isNotEmpty()) {
//                        layoutResult.data
//                    } else {
//                        Log.w(TAG, "No layout in Firebase for $busType — generating $totalSeats dynamic seats")
//                        generateDynamicSeats(busType, totalSeats)
//                    }
//
//                    // Trim to totalSeats — e.g. bus has 5 seats but template has 10
//                    val trimmed = if (totalSeats > 0 && raw.size > totalSeats) {
//                        raw.take(totalSeats)
//                    } else {
//                        raw
//                    }
//
//                    Log.d(TAG, "Layout: raw=${raw.size} trimmed=${trimmed.size} totalSeats=$totalSeats")
//
//                    if (trimmed.isEmpty()) {
//                        _uiState.value = SeatUiState.Empty
//                        return@launch
//                    }
//                    trimmed
//                }
//                is AppResult.Loading -> return@launch
//            }
//            _layout.value = finalLayout
//
//            // ── Step 2: booked seats for this date ───────────────────────
//            when (val bookedResult = seatRepository.getBookedSeatNumbers(busId, journeyDateStr)) {
//                is AppResult.NoInternet -> {
//                    _uiState.value = SeatUiState.NoInternet
//                    return@launch
//                }
//                is AppResult.Error -> {
//                    _uiState.value = SeatUiState.Error(bookedResult.message)
//                    return@launch
//                }
//                is AppResult.Success -> {
//                    _bookedSeats.value = bookedResult.data
//                    Log.d(TAG, "Booked seats: ${bookedResult.data.size}")
//
//                    // ── NEW: check if every seat is already booked ──
//                    if (_layout.value.isNotEmpty() && _layout.value.size == bookedResult.data.size) {
//                        _uiState.value = SeatUiState.Empty
//                        return@launch   // No need to start lock listeners
//                    }
//                }
//                is AppResult.Loading -> return@launch
//            }
//
//            // Both loaded — show initial seat map
//            recomputeAndEmit()
//
//            val userId = firebaseAuth.currentUser?.uid ?: ""
//
//            // ── Step 3: real-time locks (other users) ────────────────────
//            launch {
//                seatRepository.observeLockedSeats(busId, journeyDateStr, userId)
//                    .collect { locked ->
//                        _lockedByOthers.value = locked
//                        recomputeAndEmit()   // Bug 4 fix: removed duplicate _uiState.value = ... after this
//                    }
//            }
//
//            // ── Step 4: recompute when THIS user selects/deselects seats ─
//            launch {
//                combine(_layout, _bookedSeats, _lockedByOthers, _selectedSeats) { _, _, _, _ -> }
//                    .collect { recomputeAndEmit() }
//            }
//        }
//    }

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

    // ─────────────────────────────────────────────────────────────
    // recomputeAndEmit — builds List<SeatUIModel> and emits Success
    // Bug 1 fix: SeatUiState.Success now holds List<SeatUIModel>
    // Only updates state when not in Error/NoInternet/Loading
    // ─────────────────────────────────────────────────────────────
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

    // ─────────────────────────────────────────────────────────────
    // onSeatClick — select or deselect, lock or release
    // ─────────────────────────────────────────────────────────────
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

    // ─────────────────────────────────────────────────────────────
    // startLockTimer — auto-releases lock after 10 min
    // ─────────────────────────────────────────────────────────────
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

    // Show error briefly then restore Success with current seats
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

    // ─────────────────────────────────────────────────────────────
    // releaseAllSelectedSeats — on back press / screen dispose
    // ─────────────────────────────────────────────────────────────
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

    // ─────────────────────────────────────────────────────────────
    // generateDynamicSeats — fallback when Firebase layout is empty
    // Uses bus.totalSeats to create exactly the right number of seats
    // For AC Sleeper:     L1/R1, L2/R2 ... up to totalSeats
    // For AC Non Sleeper: A1-A4, B1-B4 ... up to totalSeats
    // ─────────────────────────────────────────────────────────────
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