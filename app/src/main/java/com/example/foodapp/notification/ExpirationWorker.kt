package com.example.foodapp.notification

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

// Tämä Worker suoritetaan WorkManagerin määräämänä ajankohtana
class ExpirationWorker(
    context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    override fun doWork(): Result {

        // Haetaan tuotteen nimi WorkManagerilta
        val productName = inputData.getString("productName")
            ?: return Result.failure()

        // Näytetään ilmoitus
        NotificationHelper.showNotification(
            applicationContext,
            productName
        )

        // Kerrotaan WorkManagerille,
        // että tehtävä suoritettiin onnistuneesti
        return Result.success()
    }
}

