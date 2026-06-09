package com.example.primebus.features.auth.otp

interface OtpProvider {
    fun sendOtp(phone: String): String
    fun verifyOtp(inputOtp: String): Boolean
}