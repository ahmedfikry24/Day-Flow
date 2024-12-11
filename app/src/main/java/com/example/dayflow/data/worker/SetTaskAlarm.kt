package com.example.dayflow.data.worker

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.dayflow.data.local.entity.TaskEntity

fun setTaskAlarm(context: Context, alarmManager: AlarmManager,task: TaskEntity,time:Long) {

    val intent = Intent(context, AlarmReceiver::class.java).apply {
        putExtra(TASK_ID, task.id)
        putExtra(TASK_TITLE, task.title)
    }

    val pendingIntent = PendingIntent.getBroadcast(
        context,
        task.id,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        if (alarmManager.canScheduleExactAlarms())
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                time,
                pendingIntent
            )
    } else {
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            time,
            pendingIntent
        )
    }
}