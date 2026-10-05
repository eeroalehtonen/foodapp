package com.example.foodapp.notification

import android.content.Context
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

object ExpirationScheduler {

    fun scheduleExpirationNotification(
        context: Context,
        productName: String,
        expirationDate: String
    ) {
        // Normaali ajastus (esim. laskettu viive expirationDate-päivään)
        val data = Data.Builder()
            .putString("product_name", productName)
            .build()

        val workRequest = OneTimeWorkRequestBuilder<ExpirationWorker>()
            .setInputData(data)
            .setInitialDelay(1, TimeUnit.DAYS) // Esimerkki normaalityöstä
            .build()

        WorkManager.getInstance(context).enqueue(workRequest)
    }

    // Pikatesti ilmoitusjärjestelmän toimivuudelle
    fun scheduleTestNotification(context: Context, productName: String) {
        val data = Data.Builder()
            .putString("product_name", productName)
            .build()

        val testWorkRequest = OneTimeWorkRequestBuilder<ExpirationWorker>()
            .setInputData(data)
            .setInitialDelay(5, TimeUnit.SECONDS) // Lähetetään 5s kuluttua
            .build()

        WorkManager.getInstance(context).enqueue(testWorkRequest)
    }
}