package com.example.dayflow.service

import android.content.Context
import android.content.Intent
import com.example.dayflow.data.utils.DataConstants

object DefaultServiceManager {

    fun createSessionService(context: Context, duration: Long) {
        val intent = Intent(context, WorkSessionService::class.java).apply {
            putExtra(DataConstants.SESSION_DURATION_KEY, duration)
        }
        context.startForegroundService(intent)
    }

    fun cancelSessionService(context: Context) {
        val intent = Intent(context, WorkSessionService::class.java)
        context.stopService(intent)
    }
}