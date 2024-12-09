package com.example.dayflow.ui.utils

import com.example.dayflow.data.utils.TaskPriority
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.ZoneId

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
    val taskLocalDate = LocalDate.parse(this)
    return taskLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
}

fun Long.convertLongToDate(): String {
    val localDate = LocalDate.ofEpochDay(this / (24 * 60 * 60 * 1000))
    return localDate.toString()
}