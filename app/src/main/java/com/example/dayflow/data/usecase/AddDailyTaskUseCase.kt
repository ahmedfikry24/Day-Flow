package com.example.dayflow.data.usecase

import android.content.Context
import com.example.dayflow.data.alarm.scheduleTaskAlarm
import com.example.dayflow.data.local.entity.TaskEntity
import com.example.dayflow.data.repository.Repository
import javax.inject.Inject

class AddDailyTaskUseCase @Inject constructor(
    private val repository: Repository,
    private val context: Context,
) {
    suspend operator fun invoke(task: TaskEntity) {
        scheduleTaskAlarm(context, task)
        repository.addTask(task)
    }
}
