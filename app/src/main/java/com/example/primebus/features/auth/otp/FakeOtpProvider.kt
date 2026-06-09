package com.example.primebus.features.auth.otp

import android.content.Context

class FakeOtpProvider(
    private val context: Context
) : OtpProvider {

    private var generatedOtp = ""

    override fun sendOtp(phone: String): String {
        generatedOtp = (1000..9999).random().toString()

        NotificationHelper.showOtpNotification(
            context = context,
            otp = generatedOtp
        )

        return generatedOtp
    }

    override fun verifyOtp(inputOtp: String): Boolean {
        return inputOtp == generatedOtp
    }
}