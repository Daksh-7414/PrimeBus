package com.example.primebus.features.auth.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.primebus.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class AuthState {

    object Idle : AuthState()

    object Loading : AuthState()

    object Success : AuthState()

    data class Error(val message: String) : AuthState()
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow<AuthState>(AuthState.Idle)
    val state = _state.asStateFlow()

    // ================= CHECK AUTH =================
    fun checkAuthStatus(): Boolean {
        return authRepository.isUserLoggedIn()
    }

    // ================= USER =================
    fun getUserId(): String? {
        return authRepository.getCurrentUserId()
    }

    // ================= LOGOUT =================
    fun logout() {
        authRepository.logout()
        _state.value = AuthState.Idle
    }

    // ================= GOOGLE LOGIN =================
    fun signInWithGoogle(
        idToken: String
    ) {
        viewModelScope.launch {
            _state.value = AuthState.Loading
            val result = authRepository.signInWithGoogle(idToken)
            result.onSuccess {
                _state.value = AuthState.Success
            }
            result.onFailure {
                _state.value =
                    AuthState.Error(
                        it.message ?: "Google Login Failed"
                    )
            }
        }
    }

    // ================= OTP =================
    fun initOtp(context: Context) {
        authRepository.initOtp(context)
    }

    fun sendOtp(phone: String) {
        authRepository.sendOtp(phone)
    }

    fun verifyOtp(otp: String) {
        viewModelScope.launch {
            _state.value = AuthState.Loading
            if (authRepository.verifyOtp(otp)) {
                val result = authRepository.loginWithOtp()
                result.onSuccess {
                    _state.value = AuthState.Success
                }
                result.onFailure {
                    _state.value =
                        AuthState.Error(
                            it.message ?: "OTP Login Failed"
                        )
                }
            } else {
                _state.value = AuthState.Error("Invalid OTP")
            }
        }
    }

    // ================= RESET STATE =================
    fun resetState() {
        _state.value = AuthState.Idle
    }
}