package com.example.dayflow.data.usecase

import com.example.dayflow.data.alarm.DefaultAlarmManager
import com.example.dayflow.data.repository.Repository
import javax.inject.Inject

class UpdateDailyTaskStatusUseCase @Inject constructor(
    private val repository: Repository,
    private val alarmManager: DefaultAlarmManager
) {

    suspend operator fun invoke(id: Int) {
        repository.updateDailyTaskStatus(id, true)
        alarmManager.cancelAlarm(id)
    }
}
