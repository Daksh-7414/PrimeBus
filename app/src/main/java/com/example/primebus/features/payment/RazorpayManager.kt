/*
package com.example.primebus.features.payment

import android.app.Activity
import com.example.primebus.MainActivity
import com.example.primebus.core.utils.Constants
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import org.json.JSONObject

class RazorpayManager : PaymentResultListener{

    fun openPayment(
        activity: MainActivity,    // ← typed as MainActivity, not Activity
        amount: Int,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        // Register callbacks on the Activity — this is where Razorpay sends results
//        activity.onPaymentSuccess() = onSuccess
//        activity.onPaymentError   = onError

        // Preload for faster checkout open
        Checkout.preload(activity.applicationContext)

        val checkout = Checkout()
        checkout.setKeyID(Constants.RAZORPAY_KEY)

        val options = JSONObject().apply {
            put("name",        "PrimeBus")
            put("description", "Bus Ticket Booking")
            put("currency",    "INR")
            put("amount",      amount)     // must be in paise — ₹350 = 35000
            put("prefill", JSONObject().apply {
                put("email", "")
                put("contact", "")
            })
            put("theme", JSONObject().apply {
                put("color", "#3D3BC4")
            })
        }

        try {
            checkout.open(activity, options)
        } catch (e: Exception) {
            onError("Failed to open payment: ${e.message}")
        }
    }

    override fun onPaymentSuccess(p0: String?) {
        TODO("Not yet implemented")
    }

    override fun onPaymentError(p0: Int, p1: String?) {
        TODO("Not yet implemented")
    }
}
 */
package com.example.primebus.features.payment

import android.util.Log
import com.example.primebus.MainActivity
import com.example.primebus.core.utils.Constants
import com.razorpay.Checkout
import org.json.JSONObject

// ─────────────────────────────────────────────────────────────────────────────
// RazorpayManager — ONLY opens the Razorpay checkout UI
//
// Does NOT implement PaymentResultListener — that MUST be the Activity.
// Razorpay SDK sends results to whichever Activity called checkout.open().
// We store the lambdas ON the Activity so MainActivity can forward them here.
// ─────────────────────────────────────────────────────────────────────────────
class RazorpayManager {

    companion object {
        private const val TAG = "RazorpayManager"
    }

    fun openPayment(
        activity: MainActivity,
        amount: Int,                          // paise — ₹350 = 35000
        onSuccess: (String) -> Unit,          // called with paymentId on success
        onError: (String) -> Unit             // called with reason string on failure
    ) {
        Log.d(TAG, "openPayment: amount=$amount paise")

        // ✅ Store lambdas ON the Activity
        // When Razorpay calls MainActivity.onPaymentSuccess(paymentId),
        // it will invoke this lambda — completing the bridge
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
            // ✅ Pass activity — Razorpay calls activity.onPaymentSuccess/Error
            // Do NOT pass RazorpayManager — it would never receive the callbacks
            checkout.open(activity, options)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to open Razorpay: ${e.message}")
            // Clear callbacks and report error immediately
            activity.paymentSuccessCallback = null
            activity.paymentErrorCallback   = null
            onError("Failed to open payment: ${e.message}")
        }
    }
}