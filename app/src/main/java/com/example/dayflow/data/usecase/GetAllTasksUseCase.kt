package com.example.dayflow.data.usecase

import com.example.dayflow.data.local.entity.TaskEntity
import com.example.dayflow.data.repository.Repository
import com.example.dayflow.data.utils.TaskPriority
import com.example.dayflow.ui.utils.getTaskPriority
import javax.inject.Inject

class GetAllTasksUseCase @Inject constructor(private val repository: Repository) {

    suspend operator fun invoke(): List<TaskEntity> {
        val tasks = repository.getAllTasks()
        tasks.sortedByDescending { task ->
            when (task.date.getTaskPriority()) {
                TaskPriority.HIGH -> 2
                TaskPriority.NORMAl -> 1
            }
        }
        return tasks
    }
}