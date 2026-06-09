package com.example.primebus.features.auth.otp

import android.content.BroadcastReceiver
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast

class CopyOtpReceiver : BroadcastReceiver() {
    override fun onReceive(
        context: Context,
        intent: Intent
    ) {
        val otp = intent.getStringExtra("otp") ?: return

        val clipboard =
            context.getSystemService(Context.CLIPBOARD_SERVICE)
                    as ClipboardManager

        clipboard.setPrimaryClip(
            ClipData.newPlainText("OTP", otp)
        )

        Toast.makeText(
            context,
            "OTP copied",
            Toast.LENGTH_SHORT
        ).show()
    }
}