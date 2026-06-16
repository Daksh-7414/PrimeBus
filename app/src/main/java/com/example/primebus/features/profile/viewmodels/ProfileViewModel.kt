package com.example.primebus.features.profile.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.primebus.data.models.UserModel
import com.example.primebus.data.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository
) : ViewModel() {

    var name by mutableStateOf("")
        private set

    var dob by mutableStateOf("")
        private set

    var gender by mutableStateOf("")
        private set

    var state by mutableStateOf("")
        private set

    var phone by mutableStateOf("")
        private set

    var email by mutableStateOf("")
        private set

    fun updateName(value: String) {
        name = value
    }

    fun updateDob(value: String) {
        dob = value
    }

    fun updateGender(value: String) {
        gender = value
    }

    fun updateState(value: String) {
        state = value
    }

    fun updatePhone(value: String) {
        phone = value
    }

    fun loadProfile() {

        viewModelScope.launch {
            profileRepository
                .getUserProfile()
                .onSuccess { user ->
                    name = user.userName
                    dob = user.dob
                    gender = user.gender
                    state = user.residentState
                    phone = user.phoneNumber
                    email =
                        user.email.ifBlank {
                            profileRepository.getCurrentUserEmail()
                        }
                }
        }
    }

    fun saveProfile(
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {

        viewModelScope.launch {
            val userModel = UserModel(
                userName = name,
                dob = dob,
                gender = gender,
                residentState = state,
                phoneNumber = phone,
                email = email
            )
            profileRepository
                .saveUserProfile(userModel)
                .onSuccess {
                    onSuccess()
                }
                .onFailure {
                    onFailure(
                        it.message ?: "Failed"
                    )
                }
        }
    }
}