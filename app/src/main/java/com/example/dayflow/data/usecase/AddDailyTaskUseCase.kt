package com.example.dayflow.data.usecase

import com.example.dayflow.broadcasts.DefaultAlarmManager
import com.example.dayflow.data.local.entity.DailyTaskEntity
import com.example.dayflow.data.repository.Repository
import com.example.dayflow.ui.utils.convertLongToDate
import com.example.dayflow.ui.utils.convertLongToTime
import com.example.dayflow.ui.utils.getAlarmTime
import javax.inject.Inject

class AddDailyTaskUseCase @Inject constructor(
    private val repository: Repository,
    private val alarmManager: DefaultAlarmManager,
) {
    suspend operator fun invoke(task: DailyTaskEntity) {
        if (task.date != null && task.time != null) {
            val time = getAlarmTime(task.date.convertLongToDate(), task.time.convertLongToTime())
            alarmManager.setAlarm(task.id, task.title, time)
        }
        repository.addDailyTask(task)
    }
}
