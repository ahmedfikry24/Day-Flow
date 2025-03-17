package com.example.dayflow.service.work_manager

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.dayflow.broadcasts.DefaultAlarmManager
import com.example.dayflow.data.repository.Repository
import com.example.dayflow.ui.utils.convertLongToDate
import com.example.dayflow.ui.utils.convertLongToTime
import com.example.dayflow.ui.utils.getAlarmTime
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class AlarmRescheduleWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val repository: Repository,
    private val alarmManager: DefaultAlarmManager
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val tasks = repository.getAllDailyTasks()
        for (task in tasks) {
            if (task.date != null && task.time != null) {
                val time =
                    getAlarmTime(task.date.convertLongToDate(), task.time.convertLongToTime())
                if (time > System.currentTimeMillis())
                    alarmManager.setAlarm(task.id, task.title, time)
            }
        }
        return Result.success()
    }
}
