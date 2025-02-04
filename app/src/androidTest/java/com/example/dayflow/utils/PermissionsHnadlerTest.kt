package com.example.dayflow.utils

import android.os.Build
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector

fun grantNotificationPermission() {
    val instrumentation = InstrumentationRegistry.getInstrumentation()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val permission =
            UiDevice.getInstance(instrumentation).findObject(UiSelector().text("Allow"))
        if (permission.exists()) permission.click()
    }
}

fun denyNotificationPermission() {
    val instrumentation = InstrumentationRegistry.getInstrumentation()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val permission = UiDevice.getInstance(instrumentation).findObject(UiSelector().text("Deny"))
        if (permission.exists()) permission.click()
    }
}