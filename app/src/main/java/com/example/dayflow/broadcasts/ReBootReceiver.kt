package com.example.dayflow.broadcasts

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.dayflow.service.work_manager.AlarmRescheduleWorker

class ReBootReceiver : BroadcastReceiver() {
    override fun onReceive(con: Context?, inten: Intent?) {
        inten?.let { intent ->
            if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
                con?.let { context ->
                    WorkManager.Companion.getInstance(context).enqueueUniqueWork(
                        "RescheduleAlarms",
                        ExistingWorkPolicy.REPLACE,
                        OneTimeWorkRequestBuilder<AlarmRescheduleWorker>().build()
                    )
                }
            }
        }
    }
}