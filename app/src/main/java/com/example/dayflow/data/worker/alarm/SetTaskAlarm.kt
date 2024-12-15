package com.example.dayflow.data.worker.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.dayflow.MainActivity
import com.example.dayflow.data.local.entity.TaskEntity
import com.example.dayflow.data.utils.DataConstants

fun setTaskAlarm(context: Context, alarmManager: AlarmManager, task: TaskEntity) {

    val intent = Intent(context, AlarmReceiver::class.java).apply {
        action = DataConstants.TRIGGER_NOTIFICATION_ACTION
        putExtra(DataConstants.TASK_ID, task.id)
        putExtra(DataConstants.TASK_TITLE, task.title)
    }

    val pendingIntent = PendingIntent.getBroadcast(
        context,
        task.id,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    val activity = Intent(context, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    val activityPendingIntent = PendingIntent.getActivity(
        context,
        task.id + 1000,
        activity,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        if (alarmManager.canScheduleExactAlarms()) {
            alarmManager.setAlarmClock(
                AlarmManager.AlarmClockInfo(
                    task.time!!,
                    activityPendingIntent
                ),
                pendingIntent
            )
        }
    } else {
        alarmManager.setAlarmClock(
            AlarmManager.AlarmClockInfo(
                task.time!!,
                activityPendingIntent
            ),
            pendingIntent
        )
    }
}