package com.example.dayflow.ui.utils

import android.app.Activity
import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Parcelable
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
import java.util.Calendar
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
    try {
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val time = timeFormat.parse(this)
        return time?.time ?: 0L
    } catch (e: Throwable) {
        return 0L
    }
}

fun Long.convertLongToTime(): String {
    val time = Instant.ofEpochMilli(this)
        .atZone(ZoneId.systemDefault())
        .toLocalTime()
    return time.format(DateTimeFormatter.ofPattern("HH:mm"))
}

fun String.formatTimeDigits(): String {
    val parts = this.split(":")
    val hours = parts[0].padStart(2, '0')
    val minutes = parts[1].padStart(2, '0')
    return "$hours:$minutes"
}

fun getAlarmTime(date: String, timeString: String): Long {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
    val parsedDate = dateFormat.parse("$date $timeString")
    val calendar = Calendar.getInstance()
    calendar.time = parsedDate
    return calendar.timeInMillis
}

fun generateRandomId(): Int {
    return (UiConstants.lastDailyTaskId..Int.MAX_VALUE).random()
}


fun Int.convertSessionTimeToLong(): Long {
    return this * 60 * 1000L
}

fun Long.formatSessionTime(): String {
    val seconds = (this / 1000) % 60
    val minutes = (this / 1000) / 60
    val hours = (this / 1000) / 60 / 60
    return String.format(Locale("en", "us"), "%02d:%02d:%02d", hours, minutes, seconds)
}

inline fun <reified T : Parcelable> Intent.parcelable(key: String): T? = when {
    SDK_INT >= 33 -> getParcelableExtra(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelableExtra(key) as? T
}