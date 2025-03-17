package com.example.dayflow.service.alarm

import android.app.Service
import android.content.Intent

class AlarmService : Service() {

    override fun onBind(p0: Intent?) = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }
}