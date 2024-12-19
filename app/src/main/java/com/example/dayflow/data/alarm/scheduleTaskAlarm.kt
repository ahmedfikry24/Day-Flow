package com.example.dayflow.data.alarm

import android.content.Context
import com.example.dayflow.data.local.entity.DailyTaskEntity
import com.example.dayflow.ui.utils.convertLongToDate
import com.example.dayflow.ui.utils.convertLongToTime
import com.example.dayflow.ui.utils.getAlarmTime

fun scheduleTaskAlarm(context: Context, task: DailyTaskEntity) {
    if (task.date == null || task.time == null) return

    val alarmTime = getAlarmTime(task.date.convertLongToDate(), task.time.convertLongToTime())
    DefaultAlarmManager.setAlarm(context, task.id, task.title, alarmTime)
}

