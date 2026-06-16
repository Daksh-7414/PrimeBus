package com.example.primebus.features.payment

import android.util.Log
import com.example.primebus.MainActivity
import com.example.primebus.core.utils.Constants
import com.razorpay.Checkout
import org.json.JSONObject


class RazorpayManager {

    companion object {
        private const val TAG = "RazorpayManager"
    }

    fun openPayment(
        activity: MainActivity,
        amount: Int,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        Log.d(TAG, "openPayment: amount=$amount paise")
        activity.paymentSuccessCallback = onSuccess
        activity.paymentErrorCallback   = onError

        Checkout.preload(activity.applicationContext)

        val checkout = Checkout()
        checkout.setKeyID(Constants.RAZORPAY_KEY)

        val options = JSONObject().apply {
            put("name",        "PrimeBus")
            put("description", "Bus Ticket Booking")
            put("currency",    "INR")
            put("amount",      amount)
            put("prefill", JSONObject().apply {
                put("email",   "")
                put("contact", "")
            })
            put("theme", JSONObject().apply {
                put("color", "#3D3BC4")
            })
        }

        try {

            checkout.open(activity, options)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to open Razorpay: ${e.message}")
            activity.paymentSuccessCallback = null
            activity.paymentErrorCallback   = null
            onError("Failed to open payment: ${e.message}")
        }
    }
}