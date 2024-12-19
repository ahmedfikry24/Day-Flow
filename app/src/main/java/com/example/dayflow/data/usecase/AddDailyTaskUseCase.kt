package com.example.dayflow.data.usecase

import android.content.Context
import com.example.dayflow.data.alarm.scheduleTaskAlarm
import com.example.dayflow.data.local.entity.DailyTaskEntity
import com.example.dayflow.data.repository.Repository
import javax.inject.Inject

class AddDailyTaskUseCase @Inject constructor(
    private val repository: Repository,
    private val context: Context,
) {
    suspend operator fun invoke(task: DailyTaskEntity) {
        scheduleTaskAlarm(context, task)
        repository.addTask(task)
    }
}
