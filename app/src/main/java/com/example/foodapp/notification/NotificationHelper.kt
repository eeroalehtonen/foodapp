package com.example.foodapp.notification

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.foodapp.R

object NotificationHelper {

    private const val CHANNEL_ID = "expiration_channel_v2" // Vaihdettu ID kanavan nollaamiseksi

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Expiration notifications",
                NotificationManager.IMPORTANCE_HIGH // KORKEA TÄRKEYSASTE
            ).apply {
                description = "Notifications about expiring food"
            }

            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
        }
    }

    fun showNotification(context: Context, productName: String) {
        // Luvan tarkistus Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info) // Varma vakioikoni testaukseen
            .setContentTitle("Tuote vanhenemassa")
            .setContentText("$productName vanhenee pian!")
            .setPriority(NotificationCompat.PRIORITY_HIGH) // KORKEA PRIORITEETTI
            .setDefaults(NotificationCompat.DEFAULT_ALL) // Ääni ja tärinä
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context).notify(
            System.currentTimeMillis().toInt(), // Uniikki ID jokaiselle ilmoitukselle
            notification
        )
    }
}