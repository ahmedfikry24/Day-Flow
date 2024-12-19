package com.example.dayflow.data.usecase

import android.content.Context
import com.example.dayflow.data.alarm.DefaultAlarmManager
import com.example.dayflow.data.local.entity.DailyTaskEntity
import com.example.dayflow.data.repository.Repository
import javax.inject.Inject

class DeleteDailyTaskUseCase @Inject constructor(
    private val repository: Repository,
    private val context: Context,
) {
    suspend operator fun invoke(task: DailyTaskEntity) {
        repository.deleteDailyTask(task.id)
        if (task.date != null && task.time != null)
            DefaultAlarmManager.cancelAlarm(context, task.id)
    }
}
