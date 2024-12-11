package com.example.dayflow.data.worker

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            val taskId = it.getIntExtra(TASK_ID, -1)
            val taskTitle = it.getStringExtra(TASK_TITLE)
            Log.e("AlarmReceiver", "onReceive: alarm here")
        }
    }
}
