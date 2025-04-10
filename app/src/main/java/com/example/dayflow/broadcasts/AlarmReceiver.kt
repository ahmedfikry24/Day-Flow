package com.example.dayflow.broadcasts

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
import com.example.dayflow.data.local.data_store.DataStoreManager
import com.example.dayflow.data.utils.DataConstants
import com.example.dayflow.notifications.DefaultNotificationManager
import com.example.dayflow.notifications.NotificationArgs
import com.example.dayflow.service.alarm.AlarmService
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
                    DataConstants.START_ALARM -> startAlarm(
                        context,
                        taskTitle ?: "Task Scheduled",
                        taskId
                    )

                    DataConstants.ALARM_STOP_ACTION -> cancelAlarm(context, taskId)

                    else -> Unit
                }
            }
        }
    }

    private fun startAlarm(context: Context, title: String, taskId: Int) {
        val receiverIntent = Intent(context, AlarmService::class.java).apply {
            action = DataConstants.SHOW_ALARM_OVERLAY_LAYOUT
            putExtra(DataConstants.TASK_ID, taskId)
            putExtra(DataConstants.TASK_TITLE, title)
        }
        context.startService(receiverIntent)
        showAlarmNotification(context, title, taskId)
        playRingtone(context)
    }

    private fun showAlarmNotification(context: Context, title: String, taskId: Int) {
        val notificationManager = DefaultNotificationManager(context)
        notificationManager.showNotification(
            notificationArgs = NotificationArgs(
                id = taskId,
                title = title,
                category = NotificationCompat.CATEGORY_ALARM,
                onGoing = true,
                autoCancel = true
            )
        )
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

    private fun cancelAlarm(context: Context, id: Int) {
        val notificationManager = DefaultNotificationManager(context)
        notificationManager.cancelNotification(id)
        MediaPlayerManager.stopAlarmRingtone()
    }
}