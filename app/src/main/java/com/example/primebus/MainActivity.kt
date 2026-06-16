package com.example.primebus

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.primebus.core.navigation.appnavigation.RootNavigation
import com.example.primebus.ui.theme.PrimeBusTheme
import com.razorpay.PaymentResultListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class MainActivity : ComponentActivity(), PaymentResultListener {

    companion object {
        private const val TAG = "MainActivity"
    }
    var paymentSuccessCallback: ((String) -> Unit)? = null
    var paymentErrorCallback:   ((String) -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            PrimeBusTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color    = MaterialTheme.colorScheme.background
                ) {
                    AppNavigator()
                }
            }
        }
    }

    override fun onPaymentSuccess(p0: String?) {
        Log.d(TAG, "onPaymentSuccess: paymentId=$p0")

        if (p0.isNullOrBlank()) {
            Log.e(TAG, "Razorpay returned null paymentId on success")
            paymentErrorCallback?.invoke("Payment succeeded but ID was missing. Contact support.")
            clearCallbacks()
            return
        }

        paymentSuccessCallback?.invoke(p0)
        clearCallbacks()
    }

    override fun onPaymentError(p0: Int, p1: String?) {
        Log.e(TAG, "onPaymentError: code=$p0 | response=$p1")

        val reason = when (p0) {
            0 -> "Payment cancelled"
            1 -> "Network error. Check your connection and try again."
            2 -> "Invalid payment options"
            else -> p1 ?: "Payment failed (code $p0)"
        }

        paymentErrorCallback?.invoke(reason)
        clearCallbacks()
    }

    private fun clearCallbacks() {
        paymentSuccessCallback = null
        paymentErrorCallback   = null
    }
}

@Composable
fun AppNavigator() {
    var showSplash by remember { mutableStateOf(true) }
    if (showSplash) {
        Splash()
        LaunchedEffect(Unit) {
            delay(2000)
            showSplash = false
        }
    } else {
        RootNavigation()
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAppNavigator() {
    PrimeBusTheme {
        AppNavigator()
    }
}