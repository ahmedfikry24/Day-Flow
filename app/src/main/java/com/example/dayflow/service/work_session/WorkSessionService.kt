package com.example.dayflow.service.work_session

import android.app.Service
import android.content.Intent
import android.os.CountDownTimer
import com.example.dayflow.data.utils.DataConstants
import com.example.dayflow.notifications.DefaultNotificationManager
import com.example.dayflow.notifications.NotificationArgs

class WorkSessionService : Service() {

    private var countDownTimer: CountDownTimer? = null
    private val notificationTitle = "Session Timer"
    private val defaultNotificationManager by lazy { DefaultNotificationManager(applicationContext) }

    override fun onBind(p0: Intent?) = null

    override fun onStartCommand(int: Intent?, flags: Int, startId: Int): Int {
        int?.let { intent ->
            val duration = intent.getLongExtra(DataConstants.SESSION_DURATION_KEY, 0L)
            if (duration > 0) {
                val notificationArgs = NotificationArgs(
                    id = DataConstants.SESSION_NOTIFICATION_ID,
                    title = notificationTitle,
                )
                startForeground(
                    DataConstants.SESSION_NOTIFICATION_ID,
                    defaultNotificationManager.createNotification(notificationArgs)
                )
                startTimer(duration)
            } else
                stopSelf()
        }

        return START_NOT_STICKY
    }

    private fun startTimer(duration: Long) {
        countDownTimer = object : CountDownTimer(duration, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val hour = (millisUntilFinished / 1000) / 60 / 60
                val minutes = (millisUntilFinished / 1000) / 60
                val seconds = (millisUntilFinished / 1000) % 60
                val notificationText = "Time left: ${if (hour > 0) "$hour:" else ""}$minutes:${
                    seconds.toString().padStart(2, '0')
                }"

                val notificationArgs = NotificationArgs(
                    id = DataConstants.SESSION_NOTIFICATION_ID,
                    title = notificationTitle,
                    text = notificationText,
                    isSilent = true
                )
                defaultNotificationManager.showNotification(notificationArgs)

            }

            override fun onFinish() {
                defaultNotificationManager.cancelNotification(DataConstants.SESSION_NOTIFICATION_ID)
                stopSelf()
            }

        }.start()
    }

    override fun onDestroy() {
        countDownTimer?.cancel()
        super.onDestroy()
    }
}