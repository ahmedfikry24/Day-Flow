package com.example.dayflow.data.worker

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

fun scheduleDailyWorkManagerRequest(context: Context) {
    val workRequest = PeriodicWorkRequestBuilder<TaskAlarmWorker>(1, TimeUnit.DAYS)
        .setInitialDelay(0, TimeUnit.SECONDS)
        .build()
    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        "daily_task_worker",
        ExistingPeriodicWorkPolicy.KEEP,
        workRequest
    )
}