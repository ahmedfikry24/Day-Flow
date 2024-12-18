package com.example.dayflow.data.utils

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.dayflow.R

object DefaultNotificationManager {

    fun showNotification(context: Context, notificationArgs: NotificationArgs) {
        if (
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channel = NotificationChannel(
                DataConstants.NOTIFICATION_CHANNEL_ID,
                DataConstants.NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.enableVibration(true)
            notificationManager.createNotificationChannel(channel)

            val notificationBuilder =
                NotificationCompat.Builder(context, DataConstants.NOTIFICATION_CHANNEL_ID)
                    .setSmallIcon(R.drawable.logo)
                    .setContentTitle(notificationArgs.title)
                    .setSound(notificationArgs.ringtone)
                    .setAutoCancel(true)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setDefaults(NotificationCompat.DEFAULT_ALL)

            if (notificationArgs.actionPendingIntent != null)
                notificationBuilder.addAction(
                    R.drawable.ic_check_false,
                    notificationArgs.actionText,
                    notificationArgs.actionPendingIntent
                )

            notificationManager.notify(notificationArgs.id, notificationBuilder.build())
        }
    }

    fun cancelNotification(context: Context, id: Int) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(id)
    }
}