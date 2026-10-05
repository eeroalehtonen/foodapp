package com.example.foodapp.notification

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class ExpirationWorker(
    context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    override fun doWork(): Result {
        val productName = inputData.getString("product_name") ?: "Tuote"

        NotificationHelper.showNotification(
            context = applicationContext,
            productName = productName
        )

        return Result.success()
    }
}