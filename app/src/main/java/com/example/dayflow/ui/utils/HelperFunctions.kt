package com.example.dayflow.ui.utils

import android.app.Activity
import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
import androidx.core.content.getSystemService
import com.example.dayflow.data.utils.TaskPriority
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

fun String.validateRequireField(): Boolean {
    return this.isNotBlank()
}

fun Long?.getTaskPriority(): TaskPriority {

    if (this == null) return TaskPriority.NORMAl

    val startOfWeek = LocalDate.now().with(DayOfWeek.SATURDAY)
        .atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
    val endOfWeek = LocalDate.now().with(DayOfWeek.THURSDAY)
        .atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()

    return when {
        this in startOfWeek..endOfWeek -> TaskPriority.HIGH
        else -> TaskPriority.NORMAl
    }
}

fun String.convertDateToLong(): Long {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val date = dateFormat.parse(this)
    return date?.time ?: 0L
}

fun Long.convertLongToDate(): String {
    val instant = Instant.ofEpochMilli(this)
    val localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate()
    return localDate.toString()
}


fun Context.checkScheduleAlarmPermission(): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val alarmManager = this.getSystemService<AlarmManager>() as AlarmManager
        return alarmManager.canScheduleExactAlarms()
    } else true
}

fun Context.requestScheduleAlarmPermission() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
        this.startActivity(Intent(ACTION_REQUEST_SCHEDULE_EXACT_ALARM))
}


fun Activity.goToSettings() {
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", this.packageName, null)
    ).also(::startActivity)
}

fun String.convertTimeToLong(): Long {
    val timeFormat = SimpleDateFormat("H:m", Locale.getDefault())
    val time = timeFormat.parse(this)
    return time?.time ?: 0L
}

fun Long.convertLongToTime(): String {
    val time = Instant.ofEpochMilli(this)
        .atZone(ZoneId.systemDefault())
        .toLocalTime()
    return time.format(DateTimeFormatter.ofPattern("H:m"))
}