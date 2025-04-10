package com.example.dayflow.ui.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.PowerManager
import android.provider.Settings
import androidx.core.content.getSystemService
import androidx.core.net.toUri

fun Context.isScheduleAlarmPermissionGranted(): Boolean {
    return if (VERSION.SDK_INT >= VERSION_CODES.S) {
        val alarmManager = this.getSystemService<AlarmManager>() as AlarmManager
        return alarmManager.canScheduleExactAlarms()
    } else true
}

fun Context.isBatteryOptimizationEnabled(): Boolean {
    val powerManager = this.getSystemService(Context.POWER_SERVICE) as PowerManager
    val packageName = this.packageName
    return !powerManager.isIgnoringBatteryOptimizations(packageName)
}

@SuppressLint("BatteryLife")
fun Context.requestIgnoreBatteryOptimizations() {
    val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
    intent.data = "package:${this.packageName}".toUri()
    this.startActivity(intent)
}

fun Activity.goToSettings() {
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", this.packageName, null)
    ).also(::startActivity)
}

