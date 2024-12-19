package com.example.dayflow.data.usecase

import com.example.dayflow.data.local.entity.YearlyTaskEntity
import com.example.dayflow.data.repository.Repository
import javax.inject.Inject

class GetAllYearlyTasksUseCase @Inject constructor(private val repository: Repository) {

    suspend operator fun invoke(): List<YearlyTaskEntity> {
        val tasks = repository.getAllYearlyTasks()
        tasks.sortedByDescending { task ->
            task.createdAt
        }
        return tasks
    }
}
