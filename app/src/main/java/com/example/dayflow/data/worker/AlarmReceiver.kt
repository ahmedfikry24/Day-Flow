package com.example.dayflow.data.worker

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(con: Context?, p1: Intent?) {
        p1?.let { intent ->
            con?.let { context ->
                val taskTitle = intent.getStringExtra(TASK_TITLE)
                showTaskReminderNotification(context, taskTitle ?: "Task Scheduled")
            }
        }
    }
}



