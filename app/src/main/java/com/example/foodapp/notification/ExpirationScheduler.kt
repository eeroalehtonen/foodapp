package com.example.foodapp.notification

import android.content.Context
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit

object ExpirationScheduler {

    // Ajastaa tuotteen vanhenemisilmoituksen
    fun scheduleExpirationNotification(
        context: Context,
        productName: String,
        expirationDate: String
    ) {

        // Päivämäärän muoto, jota sovelluksessa käytetään
        val dateFormat = SimpleDateFormat(
            "dd.MM.yyyy",
            Locale.getDefault()
        )

        // Ei hyväksytä esimerkiksi 50.20.2026 kaltaisia päivämääriä
        dateFormat.isLenient = false

        // Muutetaan String oikeaksi Date-arvoksi
        val date = try {

            dateFormat.parse(expirationDate)

        } catch (e: Exception) {

            null
        }

        // Jos päivämäärää ei voida lukea,
        // ilmoitusta ei ajasteta
        if (date == null) {
            return
        }


        // Luodaan Calendar valitusta päivämäärästä
        val notificationTime = Calendar.getInstance()

        notificationTime.time = date


        // Ilmoitus tulee YKSI PÄIVÄ ennen vanhenemista
        notificationTime.add(
            Calendar.DAY_OF_YEAR,
            -1
        )


        // Ilmoituksen kellonaika
        // Tässä klo 09:00
        notificationTime.set(
            Calendar.HOUR_OF_DAY,
            9
        )

        notificationTime.set(
            Calendar.MINUTE,
            0
        )

        notificationTime.set(
            Calendar.SECOND,
            0
        )

        notificationTime.set(
            Calendar.MILLISECOND,
            0
        )


        // Nykyinen aika
        val currentTime =
            System.currentTimeMillis()


        // Lasketaan kuinka kauan WorkManagerin pitää odottaa
        val delay =
            notificationTime.timeInMillis - currentTime


        // Jos ilmoitusaika on jo mennyt,
        // ilmoitusta ei ajasteta
        if (delay <= 0) {
            return
        }


        // Data, joka annetaan ExpirationWorkerille
        val inputData = Data.Builder()

            // ExpirationWorker hakee tämän nimellä "productName"
            .putString(
                "productName",
                productName
            )

            .build()


        // Luodaan kertaluontoinen WorkManager-tehtävä
        val notificationWork =
            OneTimeWorkRequestBuilder<ExpirationWorker>()

                // Kuinka kauan odotetaan ennen suorittamista
                .setInitialDelay(
                    delay,
                    TimeUnit.MILLISECONDS
                )

                // Annetaan tuotteen nimi Workerille
                .setInputData(inputData)

                .build()


        // Lähetetään tehtävä WorkManagerille
        WorkManager
            .getInstance(context)
            .enqueue(notificationWork)
    }
}

