package com.example.dayflow.data.utils

import android.app.PendingIntent
import android.net.Uri
import androidx.core.app.NotificationCompat

data class NotificationArgs(
    val id: Int,
    val title: String,
    val text: String? = null,
    val actionText: String = "Stop",
    val actionPendingIntent: PendingIntent? = null,
    val category: String? = null,
    val ringtone: Uri? = null,
    val isSilent: Boolean = false,
    val priority: Int = NotificationCompat.PRIORITY_HIGH
)
