package com.example.dayflow.service

import android.content.Context
import android.content.Intent
import com.example.dayflow.data.utils.DataConstants
import com.example.dayflow.service.work_session.WorkSessionService

class DefaultServiceManager(private val context: Context) {

    fun createSessionService(duration: Long) {
        val intent = Intent(context, WorkSessionService::class.java).apply {
            putExtra(DataConstants.SESSION_DURATION_KEY, duration)
        }
        context.startForegroundService(intent)
    }

    fun cancelSessionService() {
        val intent = Intent(context, WorkSessionService::class.java)
        context.stopService(intent)
    }
}