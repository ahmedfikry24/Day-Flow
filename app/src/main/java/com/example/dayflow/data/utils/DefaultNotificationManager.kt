package com.example.dayflow.data.utils

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.dayflow.MainActivity
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
            ).apply {
                enableVibration(true)
                notificationArgs.ringtone?.let { setSound(it, null) }
            }

            notificationManager.createNotificationChannel(channel)

            val activity = Intent(context, MainActivity::class.java)
            val activityPendingIntent = PendingIntent.getActivity(
                context,
                DataConstants.ACTIVITY_PENDING_ID,
                activity,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val notificationBuilder =
                NotificationCompat.Builder(context, DataConstants.NOTIFICATION_CHANNEL_ID)
                    .setSmallIcon(R.drawable.logo)
                    .setContentTitle(notificationArgs.title)
                    .setContentIntent(activityPendingIntent)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(notificationArgs.actionPendingIntent == null)
                    .setOngoing(true)

            notificationArgs.actionPendingIntent?.let {
                notificationBuilder.addAction(
                    R.drawable.ic_check_false,
                    notificationArgs.actionText,
                    notificationArgs.actionPendingIntent
                )
            }
            notificationArgs.category?.let { notificationBuilder.setCategory(it) }

            notificationManager.notify(notificationArgs.id, notificationBuilder.build())
        }
    }

    fun cancelNotification(context: Context, id: Int) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(id)
    }
}