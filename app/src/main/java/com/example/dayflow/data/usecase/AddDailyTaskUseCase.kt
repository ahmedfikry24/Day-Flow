package com.example.dayflow.data.usecase

import com.example.dayflow.data.local.entity.TaskEntity
import com.example.dayflow.data.repository.Repository
import javax.inject.Inject

class AddDailyTaskUseCase @Inject constructor(
    private val repository: Repository,
) {
    suspend operator fun invoke(task: TaskEntity) {
        repository.addTask(task)
    }
}
