package com.example.dayflow.broadcasts

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
import com.example.dayflow.data.local.data_store.DataStoreManager
import com.example.dayflow.data.utils.DataConstants
import com.example.dayflow.notifications.DefaultNotificationManager
import com.example.dayflow.notifications.NotificationArgs
import com.example.dayflow.utils.MediaPlayerManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
        val notificationManager = DefaultNotificationManager(context)
        notificationManager.showNotification(
            notificationArgs = NotificationArgs(
                id = taskId,
                title = title,
                actionPendingIntent = stopPendingIntent,
                category = NotificationCompat.CATEGORY_ALARM
            )
        )
        playRingtone(context)
    }

    private fun cancelScheduledAlarm(context: Context, id: Int) {
        val notificationManager = DefaultNotificationManager(context)
        val alarmManager = DefaultAlarmManager(context)
        alarmManager.cancelAlarm(id)
        notificationManager.cancelNotification(id)
        MediaPlayerManager.stopAlarmRingtone()
    }


    private fun playRingtone(context: Context) {
        val dataStoreManager = DataStoreManager(context)
        CoroutineScope(Dispatchers.Default).launch {
            dataStoreManager.alarmRingtone.collect {
                val uri = it?.toUri()
                MediaPlayerManager.startAlarmRingtone(context, uri)
            }
        }
    }
}