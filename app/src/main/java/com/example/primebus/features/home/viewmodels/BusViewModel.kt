/*package com.example.primebus.features.home.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.primebus.AppResult
import com.example.primebus.BusUiState
import com.example.primebus.data.models.Bus
import com.example.primebus.data.repository.BusRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BusViewModel @Inject constructor(
    private val repository: BusRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<BusUiState>(BusUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    fun refreshBuses(routeId: String) {
        viewModelScope.launch {
            _isRefreshing.value = true

            fetchBuses(routeId)

            _isRefreshing.value = false
        }
    }

    fun fetchBuses(routeId: String) {
        viewModelScope.launch {
            repository.getBusesByRoute(routeId)
                .collect { result ->
                    when (result) {

                        AppResult.Loading -> {
                            _uiState.value = BusUiState.Loading
                        }

                        AppResult.NoInternet -> {
                            _uiState.value = BusUiState.NoInternet
                        }

                        is AppResult.Error -> {
                            _uiState.value = BusUiState.Error(
                                result.message
                            )
                        }

                        is AppResult.Success -> {
                            _uiState.value =
                                if (result.data.isEmpty()) {
                                    BusUiState.Empty
                                } else {
                                    BusUiState.Success(
                                        result.data
                                    )
                                }
                        }
                    }
                }
        }
    }
}

 */
package com.example.primebus.features.home.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.primebus.AppResult
import com.example.primebus.BusFilterState
import com.example.primebus.BusSortOption
import com.example.primebus.BusTypeFilter
import com.example.primebus.BusUiState
import com.example.primebus.data.models.Bus
import com.example.primebus.data.repository.BusRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BusViewModel @Inject constructor(
    private val repository: BusRepository
) : ViewModel() {

    // Raw result from Firebase — untouched, unfiltered
    private val _rawResult = MutableStateFlow<AppResult<List<Bus>>>(AppResult.Loading)

    // Current filter/sort selections
    private val _filterState = MutableStateFlow(BusFilterState())
    val filterState = _filterState.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    // Tracks the currently active Firebase listener job so we don't
    // create duplicate listeners on refresh/re-fetch
    private var fetchJob: Job? = null

    // Derived UI state — recomputes whenever raw data OR filters change
    val uiState = combine(_rawResult, _filterState) { result, filters ->
        mapToUiState(result, filters)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = BusUiState.Loading
    )

    private fun mapToUiState(
        result: AppResult<List<Bus>>,
        filters: BusFilterState
    ): BusUiState {
        return when (result) {

            AppResult.Loading -> BusUiState.Loading

            AppResult.NoInternet -> BusUiState.NoInternet

            is AppResult.Error -> BusUiState.Error(result.message)

            is AppResult.Success -> {
                val rawBuses = result.data

                if (rawBuses.isEmpty()) {
                    return BusUiState.Empty
                }

                val filtered = applyFilters(rawBuses, filters)

                if (filtered.isEmpty()) {
                    BusUiState.NoFilterMatch
                } else {
                    BusUiState.Success(filtered)
                }
            }
        }
    }

    private fun applyFilters(
        buses: List<Bus>,
        filters: BusFilterState
    ): List<Bus> {

        var result = buses

        // ── Type filter (AC Sleeper / AC Non Sleeper) ──
        // If selectedTypes is empty -> no type filter applied (show all)
        if (filters.selectedTypes.isNotEmpty()) {
            result = result.filter { bus ->
                val matchesSleeper = filters.selectedTypes.contains(BusTypeFilter.AC_SLEEPER) &&
                        bus.type.equals("AC Sleeper", ignoreCase = true)

                val matchesNonSleeper = filters.selectedTypes.contains(BusTypeFilter.AC_NON_SLEEPER) &&
                        bus.type.equals("AC Non Sleeper", ignoreCase = true)

                matchesSleeper || matchesNonSleeper
            }
        }

        // ── Rating filter ──
        filters.minRating?.let { minRating ->
            result = result.filter { bus -> bus.rating >= minRating }
        }

        // ── Sort ──
        result = when (filters.sortOption) {
            BusSortOption.NONE -> result
            BusSortOption.PRICE_LOW_TO_HIGH -> result.sortedBy { it.price }
            BusSortOption.PRICE_HIGH_TO_LOW -> result.sortedByDescending { it.price }
            BusSortOption.RATING_HIGH_TO_LOW -> result.sortedByDescending { it.rating }
        }

        return result
    }

    // ── Filter update functions (called from UI) ──

    fun toggleTypeFilter(type: BusTypeFilter) {
        _filterState.update { current ->
            val newTypes = if (current.selectedTypes.contains(type)) {
                current.selectedTypes - type
            } else {
                current.selectedTypes + type
            }
            current.copy(selectedTypes = newTypes)
        }
    }

    fun setMinRating(minRating: Double?) {
        _filterState.update { it.copy(minRating = minRating) }
    }

    fun setSortOption(sortOption: BusSortOption) {
        _filterState.update { it.copy(sortOption = sortOption) }
    }

    fun clearAllFilters() {
        _filterState.value = BusFilterState()
    }

    // ── Data fetching ──

    /**
     * Starts listening to Firebase for this route. Safe to call multiple times —
     * if a listener is already active, this is a no-op (use refreshBuses to force).
     */
    fun fetchBuses(routeId: String) {
        // Avoid attaching a second listener if one is already running
        if (fetchJob?.isActive == true) return

        startListening(routeId)
    }

    /**
     * Pull-to-refresh: cancels any existing listener and re-attaches a fresh one.
     * _isRefreshing is set to false as soon as the FIRST result (of any kind)
     * arrives from the new listener — not when the flow completes (it never does).
     */
    fun refreshBuses(routeId: String) {
        _isRefreshing.value = true
        startListening(routeId, isRefresh = true)
    }

    private fun startListening(routeId: String, isRefresh: Boolean = false) {
        // Cancel any previous listener to avoid duplicate Firebase subscriptions
        fetchJob?.cancel()

        fetchJob = viewModelScope.launch {
            var firstTerminalEmission = true

            repository.getBusesByRoute(routeId)
                .collect { result ->

                    // During refresh, the repo immediately re-emits AppResult.Loading
                    // before the real data arrives. Skip it so the existing list
                    // stays visible (only the pull-to-refresh spinner indicates progress).
                    if (isRefresh && result is AppResult.Loading) {
                        return@collect
                    }

                    _rawResult.value = result

                    // Turn off the refresh spinner as soon as we get the first
                    // real (non-Loading) result back — success, error, or no-internet.
                    if (isRefresh && firstTerminalEmission) {
                        _isRefreshing.value = false
                        firstTerminalEmission = false
                    }
                }
        }
    }
}