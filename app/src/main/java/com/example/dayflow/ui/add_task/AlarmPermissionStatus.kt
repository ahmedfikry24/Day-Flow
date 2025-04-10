package com.example.dayflow.ui.add_task

import android.content.Context
import android.provider.Settings
import com.example.dayflow.ui.utils.isScheduleAlarmPermissionGranted

enum class AlarmPermissionStatus {
    NONE,
    SCHEDULE,
    DRAW_OVER_APPS
}

fun getNeededPermission(context: Context): AlarmPermissionStatus {
    return when {
        !context.isScheduleAlarmPermissionGranted() -> AlarmPermissionStatus.SCHEDULE
        !Settings.canDrawOverlays(context) -> AlarmPermissionStatus.DRAW_OVER_APPS
        else -> AlarmPermissionStatus.NONE
    }
}
