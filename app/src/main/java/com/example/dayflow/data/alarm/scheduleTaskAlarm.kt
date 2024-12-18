package com.example.dayflow.data.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.dayflow.data.local.entity.TaskEntity
import com.example.dayflow.data.utils.DataConstants
import com.example.dayflow.ui.utils.convertLongToDate
import com.example.dayflow.ui.utils.convertLongToTime
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

fun scheduleTaskAlarm(context: Context, task: TaskEntity) {

    if (task.date == null || task.time == null) return

    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    val intent = Intent(context, AlarmReceiver::class.java).apply {
        action = DataConstants.TRIGGER_NOTIFICATION_ACTION
        putExtra(DataConstants.TASK_ID, task.id)
        putExtra(DataConstants.TASK_TITLE, task.title)
    }

    val alarmTime = getAlarmTime(task.date.convertLongToDate(), task.time.convertLongToTime())

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

private fun getAlarmTime(dateString: String, timeString: String): Long {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val timeFormat = SimpleDateFormat("H:m", Locale.getDefault())

    val date = dateFormat.parse(dateString)
    val time = timeFormat.parse(timeString)

    val calendar = Calendar.getInstance()
    calendar.timeZone = TimeZone.getDefault()
    calendar.time = date
    val timeCalendar = Calendar.getInstance()
    timeCalendar.time = time

    calendar.set(Calendar.HOUR_OF_DAY, timeCalendar.get(Calendar.HOUR_OF_DAY))
    calendar.set(Calendar.MINUTE, timeCalendar.get(Calendar.MINUTE))
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)


    return calendar.timeInMillis
}