package com.example.dayflow.data.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.example.dayflow.data.local.entity.TaskEntity
import com.example.dayflow.data.utils.DataConstants
import com.example.dayflow.ui.utils.convertDateToLong
import com.example.dayflow.ui.utils.convertTimeToLong
import com.example.dayflow.ui.utils.ui_state.toEntity
import java.util.Calendar
import java.util.TimeZone

fun scheduleTaskAlarm(context: Context, task: TaskEntity) {

    if (task.date == null || task.time == null) return

    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    val intent = Intent(context, AlarmReceiver::class.java).apply {
        action = DataConstants.TRIGGER_NOTIFICATION_ACTION
        putExtra(DataConstants.TASK_ID, task.id)
        putExtra(DataConstants.TASK_TITLE, task.title)
    }

    val alarmTime = getAlarmTime(task.date, task.time)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        if (alarmManager.canScheduleExactAlarms()) {
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                task.id,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
            )
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                alarmTime,
                pendingIntent
            )
        }

    } else {
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            task.id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            alarmTime,
            pendingIntent
        )
    }
}

private fun getAlarmTime(date: Long, time: Long): Long {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = date + (time % 86400000)
    val timeZone = TimeZone.getDefault()
    return calendar.timeInMillis + timeZone.rawOffset
}