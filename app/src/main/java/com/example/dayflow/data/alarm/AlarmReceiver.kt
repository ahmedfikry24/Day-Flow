package com.example.dayflow.data.alarm

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.RingtoneManager
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.dayflow.R
import com.example.dayflow.data.utils.DataConstants

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(con: Context?, p1: Intent?) {
        p1?.let { intent ->
            con?.let { context ->

                val taskTitle = intent.getStringExtra(DataConstants.TASK_TITLE)
                val taskId = intent.getIntExtra(DataConstants.TASK_ID, -1)

                when (intent.action) {
                    DataConstants.TRIGGER_NOTIFICATION_ACTION -> showAlarmNotification(
                        context,
                        taskTitle ?: "Task Scheduled",
                        taskId
                    )

                    DataConstants.ALARM_STOP_ACTION -> cancelScheduledAlarm(context, taskId)

                    else -> Unit
                }
            }
        }
    }

    private fun showAlarmNotification(context: Context, title: String, taskId: Int) {
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

            val stopIntent = Intent(context, AlarmReceiver::class.java).apply {
                action = DataConstants.ALARM_STOP_ACTION
                putExtra(DataConstants.TASK_ID, taskId)
            }
            val stopPendingIntent = PendingIntent.getBroadcast(
                context,
                taskId,
                stopIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            var alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
            if (alarmUri == null)
                alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

            val notificationBuilder =
                NotificationCompat.Builder(context, DataConstants.NOTIFICATION_CHANNEL_ID)
                    .setSmallIcon(R.drawable.logo)
                    .setContentTitle(title)
                    .setSound(alarmUri)
                    .setAutoCancel(true)
                    .addAction(R.drawable.ic_check_false, "Stop", stopPendingIntent)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setDefaults(NotificationCompat.DEFAULT_ALL)

            notificationManager.notify(taskId, notificationBuilder.build())
        }
    }
}

