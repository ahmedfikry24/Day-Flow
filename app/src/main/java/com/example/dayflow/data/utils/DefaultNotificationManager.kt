package com.example.dayflow.data.utils

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.dayflow.MainActivity
import com.example.dayflow.R
import javax.inject.Inject

class DefaultNotificationManager @Inject constructor(private val context: Context) {

    fun showNotification(notificationArgs: NotificationArgs) {
        if (
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channel = createChannel(notificationArgs.ringtone)

            notificationManager.createNotificationChannel(channel)

            notificationManager.notify(
                notificationArgs.id,
                createNotification(notificationArgs)
            )
        }
    }

    fun createNotification(notificationArgs: NotificationArgs): Notification {
        val notificationBuilder =
            NotificationCompat.Builder(context, DataConstants.NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle(notificationArgs.title)
                .setPriority(notificationArgs.priority)
                .setAutoCancel(notificationArgs.actionPendingIntent == null)
                .setOngoing(true)
                .setSilent(notificationArgs.isSilent)

        notificationArgs.text?.let {
            notificationBuilder.setContentText(it)
        }
        notificationArgs.actionPendingIntent?.let {
            notificationBuilder.addAction(
                R.drawable.ic_check_false,
                notificationArgs.actionText,
                notificationArgs.actionPendingIntent
            )
        }
        notificationArgs.category?.let { notificationBuilder.setCategory(it) }

        if (notificationArgs.isClickable) {
            val activity = Intent(context, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            }
            val activityPendingIntent = PendingIntent.getActivity(
                context,
                DataConstants.ACTIVITY_PENDING_ID,
                activity,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            notificationBuilder.setContentIntent(activityPendingIntent)
        }

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel = createChannel(notificationArgs.ringtone)
        notificationManager.createNotificationChannel(channel)

        return notificationBuilder.build()
    }

    private fun createChannel(sound: Uri?): NotificationChannel {
        return NotificationChannel(
            DataConstants.NOTIFICATION_CHANNEL_ID,
            DataConstants.NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            enableVibration(true)
            sound?.let { setSound(it, null) }
        }
    }

    fun cancelNotification(id: Int) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(id)
    }
}