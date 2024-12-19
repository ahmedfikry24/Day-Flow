package com.example.dayflow.data.repository

import com.example.dayflow.data.local.entity.DailyTaskEntity
import com.example.dayflow.data.local.entity.YearlyTaskEntity

interface Repository {

    suspend fun addDailyTask(task: DailyTaskEntity)
    suspend fun getAllDailyTasks(): List<DailyTaskEntity>
    suspend fun deleteDailyTask(id: Int)
    suspend fun updateDailyTaskStatus(id: Int, status: Boolean)

    suspend fun addYearlyTask(task: YearlyTaskEntity)
    suspend fun getAllYearlyTasks(): List<YearlyTaskEntity>
    suspend fun deleteYearlyTask(id: Int)
}