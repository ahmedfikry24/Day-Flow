package com.example.dayflow.data.usecase

import com.example.dayflow.data.local.entity.DailyTaskEntity
import com.example.dayflow.data.repository.Repository
import com.example.dayflow.data.utils.TaskPriority
import com.example.dayflow.ui.utils.getTaskPriority
import javax.inject.Inject

class GetAllDailyTasksUseCase @Inject constructor(private val repository: Repository) {

    suspend operator fun invoke(): List<DailyTaskEntity> {
        val tasks = repository.getAllDailyTasks()
        tasks.sortedByDescending { task ->
            when (task.date.getTaskPriority()) {
                TaskPriority.HIGH -> 2
                TaskPriority.NORMAl -> 1
            }
        }
        return tasks
    }
}
