package com.example.dayflow.notifications

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
    val isClickable: Boolean = true,
    val onGoing: Boolean = true,
    val autoCancel: Boolean = false,
    val priority: Int = NotificationCompat.PRIORITY_HIGH,
    val visibility: Int = NotificationCompat.VISIBILITY_PUBLIC
)
