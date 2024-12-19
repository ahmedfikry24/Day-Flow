package com.example.dayflow.data.usecase

import com.example.dayflow.data.local.entity.YearlyTaskEntity
import com.example.dayflow.data.repository.Repository
import javax.inject.Inject

class AddYearlyTaskUseCase @Inject constructor(private val repository: Repository) {

    suspend operator fun invoke(task: YearlyTaskEntity) {
        repository.addYearlyTask(task)
    }
}
