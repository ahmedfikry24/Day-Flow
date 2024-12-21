package com.example.dayflow.data.usecase

import android.content.Context
import com.example.dayflow.data.alarm.DefaultAlarmManager
import com.example.dayflow.data.repository.Repository
import javax.inject.Inject

class UpdateDailyTaskStatusUseCase @Inject constructor(
    private val repository: Repository,
    private val context: Context
) {

    suspend operator fun invoke(id: Int) {
        repository.updateDailyTaskStatus(id, true)
        DefaultAlarmManager.cancelAlarm(context, id)
    }
}
