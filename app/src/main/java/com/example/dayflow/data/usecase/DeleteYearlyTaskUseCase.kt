package com.example.dayflow.data.usecase

import com.example.dayflow.data.repository.Repository
import javax.inject.Inject

class DeleteYearlyTaskUseCase @Inject constructor(private val repository: Repository) {

    suspend operator fun invoke(id: Int) {
        repository.deleteYearlyTask(id)
    }
}
