package com.example.dayflow.data.alarm

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import com.example.dayflow.data.utils.DataConstants
import com.example.dayflow.data.utils.DefaultNotificationManager
import com.example.dayflow.data.utils.MediaPlayerManager
import com.example.dayflow.data.utils.NotificationArgs

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
        val stopIntent = Intent(context, AlarmReceiver::class.java).apply {
            action = DataConstants.ALARM_STOP_ACTION
            putExtra(DataConstants.TASK_ID, taskId)
        }
        val stopPendingIntent = PendingIntent.getBroadcast(
            context,
            taskId + 1000,
            stopIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        DefaultNotificationManager.showNotification(
            context,
            notificationArgs = NotificationArgs(
                id = taskId,
                title = title,
                actionPendingIntent = stopPendingIntent,
                category = NotificationCompat.CATEGORY_ALARM
            )
        )

        MediaPlayerManager.startAlarmRingtone(
            context,
            RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        )
    }

    private fun cancelScheduledAlarm(context: Context, id: Int) {
        DefaultAlarmManager.cancelAlarm(context, id)
        DefaultNotificationManager.cancelNotification(context, id)
        MediaPlayerManager.stopAlarmRingtone()
    }
}

