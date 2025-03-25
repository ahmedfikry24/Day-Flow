package com.example.dayflow.service.alarm

import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.graphics.Point
import android.os.Build
import android.os.IBinder
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
import android.widget.Button
import android.widget.TextView
import com.example.dayflow.R
import com.example.dayflow.broadcasts.DefaultAlarmManager
import com.example.dayflow.data.utils.DataConstants
import com.example.dayflow.notifications.DefaultNotificationManager
import com.example.dayflow.utils.MediaPlayerManager

class AlarmService : Service() {

    private lateinit var windowManager: WindowManager
    private lateinit var overlayView: View
    private lateinit var alarmManager: DefaultAlarmManager
    private lateinit var notificationManager: DefaultNotificationManager

    override fun onCreate() {
        super.onCreate()
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        alarmManager = DefaultAlarmManager(applicationContext)
        notificationManager = DefaultNotificationManager(applicationContext)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            if (it.action == DataConstants.ALARM_STOP_ACTION)
                setupOverlay(it)
        }
        return START_STICKY
    }

    private fun setupOverlay(intent: Intent) {
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        overlayView = LayoutInflater.from(applicationContext)
            .inflate(R.layout.alarm_overlay_layout, null, false)
        val (width, height) = getScreenSize()
        val params = WindowManager.LayoutParams(
            width,
            height,
            TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.START
        }
        windowManager.addView(overlayView, params)


        val taskTitle = intent.getStringExtra(DataConstants.TASK_TITLE) ?: "Task Scheduled"
        val title = overlayView.findViewById<TextView>(R.id.task_title)
        title.text = intent.getStringExtra(DataConstants.TASK_TITLE)
        val taskId = intent.getIntExtra(DataConstants.TASK_ID, -1)
        overlayView.findViewById<Button>(R.id.stop_button).setOnClickListener {
            MediaPlayerManager.stopAlarmRingtone()
            notificationManager.cancelNotification(taskId)
            stopSelf()
        }
        overlayView.findViewById<Button>(R.id.snooze_button).setOnClickListener {
            MediaPlayerManager.stopAlarmRingtone()
            alarmManager.setAlarm(taskId, taskTitle, (System.currentTimeMillis() + (5 * 60 * 1000)))
            stopSelf()
        }
    }

    private fun getScreenSize(): Pair<Int, Int> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val bounds = windowManager.currentWindowMetrics.bounds
            Pair(bounds.width(), bounds.height())
        } else {
            val point = Point()
            windowManager.defaultDisplay.getRealSize(point)
            Pair(point.x, point.y)
        }
    }

    override fun onBind(p0: Intent?): IBinder? = null


    override fun onDestroy() {
        super.onDestroy()
        if (::overlayView.isInitialized) {
            windowManager.removeView(overlayView)
        }
    }
}