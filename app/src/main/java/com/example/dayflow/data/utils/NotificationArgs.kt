package com.example.dayflow.data.utils

import android.app.PendingIntent
import android.media.RingtoneManager
import android.net.Uri

data class NotificationArgs(
    val id: Int,
    val title: String,
    val actionText: String = "Stop",
    val actionPendingIntent: PendingIntent? = null,
    val ringtone: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
)
