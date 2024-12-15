package com.example.dayflow.data.worker

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.dayflow.data.utils.DataConstants
import java.util.concurrent.TimeUnit


fun scheduleDailyWorkManagerRequest(context: Context) {
    val workRequest = PeriodicWorkRequestBuilder<TaskAlarmWorker>(
        repeatInterval = 6, TimeUnit.HOURS,
        flexTimeInterval = 1, TimeUnit.HOURS
    )
        .setInitialDelay(0, TimeUnit.SECONDS)
        .build()

    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        DataConstants.WORKER_NAME,
        ExistingPeriodicWorkPolicy.KEEP,
        workRequest
    )
}