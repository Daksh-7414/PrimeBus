package com.example.primebus.data.repository

import android.content.Context
import com.example.primebus.features.auth.otp.FakeOtpProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) {

    private var otpProvider: FakeOtpProvider? = null

    fun initOtp(context: Context) {
        otpProvider = FakeOtpProvider(context)
    }

    fun sendOtp(phone: String) {
        otpProvider?.sendOtp(phone)
    }

    fun verifyOtp(otp: String): Boolean {
        return otpProvider?.verifyOtp(otp) ?: false
    }

    // ================= OTP LOGIN =================
    suspend fun loginWithOtp(): Result<Unit> {
        return try {
            firebaseAuth.signInAnonymously().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ================= GOOGLE LOGIN =================
    suspend fun signInWithGoogle(idToken: String): Result<Unit> {
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            firebaseAuth.signInWithCredential(credential).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ================= AUTH STATE =================
    fun isUserLoggedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }

    // ================= CURRENT USER UID =================
    fun getCurrentUserId(): String? {
        return firebaseAuth.currentUser?.uid
    }

    // ================= LOGOUT =================
    fun logout() {
        firebaseAuth.signOut()
    }
}