package com.example.dayflow.data.worker

import android.app.AlarmManager
import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.dayflow.data.usecase.GetAllTasksInSpecificDateUseCase
import com.example.dayflow.data.worker.alarm.setTaskAlarm
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId


@HiltWorker
class TaskAlarmWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val getAllTasksInSpecificDate: GetAllTasksInSpecificDateUseCase,
) : CoroutineWorker(context, workerParams) {

    private lateinit var alarmManager: AlarmManager

    override suspend fun doWork(): Result {
        alarmManager = applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val startOfToday = LocalDate.now()
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()

        val endOfToday = LocalDate.now()
            .atTime(LocalTime.MAX)
            .atZone(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()

        val tasks = getAllTasksInSpecificDate(startOfToday..endOfToday)
        val currentTime = System.currentTimeMillis()

        for (task in tasks) {
            if (task.time!! > currentTime)
                setTaskAlarm(applicationContext, alarmManager, task)
        }

        return Result.success()
    }
}