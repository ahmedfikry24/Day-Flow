package com.example.dayflow.data.alarm

import android.content.Context
import com.example.dayflow.data.local.entity.TaskEntity
import com.example.dayflow.ui.utils.convertLongToDate
import com.example.dayflow.ui.utils.convertLongToTime
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

fun scheduleTaskAlarm(context: Context, task: TaskEntity) {
    if (task.date == null || task.time == null) return

    val alarmTime = getAlarmTime(task.date.convertLongToDate(), task.time.convertLongToTime())
    DefaultAlarmManager.setAlarm(context, task.id, task.title, alarmTime)
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