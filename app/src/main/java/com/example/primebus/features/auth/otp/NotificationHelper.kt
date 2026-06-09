package com.example.primebus.features.auth.otp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.primebus.R

object NotificationHelper {

    fun showOtpNotification(
        context: Context,
        otp: String
    ) {
        val channelId = "otp_channel"

        val manager =
            context.getSystemService(Context.NOTIFICATION_SERVICE)
                    as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(
                NotificationChannel(
                    channelId,
                    "OTP",
                    NotificationManager.IMPORTANCE_HIGH
                )
            )
        }

        val intent = Intent(context, CopyOtpReceiver::class.java)
        intent.putExtra("otp", otp)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val action = NotificationCompat.Action.Builder(
            0,
            "Copy Code",
            pendingIntent
        ).build()

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.app_logo)
            .setContentTitle("PrimeBus OTP")
            .setContentText("Your verification code is $otp")
            .addAction(action)
            .setAutoCancel(true)
            .build()

        manager.notify(1001, notification)
    }
}