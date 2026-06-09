package com.example.primebus.features.home.viewmodels

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