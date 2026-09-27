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


// Hoitaa sovelluksen ilmoitusten luomisen ja näyttämisen
object NotificationHelper {

    // Ilmoituskanavan tunniste
    private const val CHANNEL_ID = "expiration_channel"


    // Luo ilmoituskanavan Androidille
    fun createNotificationChannel(context: Context) {

        // NotificationChannel tarvitaan Android 8.0 (API 26) ja uudemmissa
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            // Luodaan ilmoituskanava
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Expiration notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {

                // Ilmoituskanavan kuvaus
                description = "Notifications about expiring food"
            }


            // Haetaan Androidin NotificationManager
            val notificationManager =
                context.getSystemService(NotificationManager::class.java)


            // Rekisteröidään ilmoituskanava
            notificationManager.createNotificationChannel(channel)
        }
    }


    // Näyttää ilmoituksen käyttäjälle
    fun showNotification(
        context: Context,
        productName: String
    ) {

        // Rakennetaan ilmoitus
        val notification =
            NotificationCompat.Builder(
                context,
                CHANNEL_ID
            )
                // Ilmoituksen kuvake
                .setSmallIcon(R.drawable.ic_launcher_foreground)

                // Ilmoituksen otsikko
                .setContentTitle("Product expiring soon")

                // Ilmoituksen teksti
                .setContentText("$productName expires tomorrow!")

                // Ilmoituksen tärkeys
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

                // Ilmoitus poistuu, kun käyttäjä painaa sitä
                .setAutoCancel(true)

                // Rakennetaan valmis ilmoitus
                .build()


        // Android 13 ja uudemmat tarvitsevat
        // POST_NOTIFICATIONS-luvan
        if (
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            // Jos lupaa ei ole, ilmoitusta ei näytetä
            return
        }


        // Näytetään ilmoitus
        NotificationManagerCompat
            .from(context)
            .notify(
                productName.hashCode(),
                notification
            )
    }
}