package com.example.dayflow.data.usecase

import android.content.Context
import com.example.dayflow.data.alarm.cancelScheduledAlarm
import com.example.dayflow.data.local.entity.TaskEntity
import com.example.dayflow.data.repository.Repository
import javax.inject.Inject

class DeleteDailyTaskUseCase @Inject constructor(
    private val repository: Repository,
    private val context: Context,
) {
    suspend operator fun invoke(task: TaskEntity) {
        repository.deleteTask(task.id)
        if (task.date != null && task.time != null)
            cancelScheduledAlarm(context, task.id)
    }
}
