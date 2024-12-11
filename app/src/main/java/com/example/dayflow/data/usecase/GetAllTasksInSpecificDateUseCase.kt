package com.example.dayflow.data.usecase

import com.example.dayflow.data.local.entity.TaskEntity
import com.example.dayflow.data.repository.Repository
import javax.inject.Inject

class GetAllTasksInSpecificDateUseCase @Inject constructor(
    private val repository: Repository,
) {

    suspend operator fun invoke(date: LongRange): List<TaskEntity> {
        return repository.getAllTasks().filter {
            it.date != null && !it.status && it.date in date
        }
    }
}
