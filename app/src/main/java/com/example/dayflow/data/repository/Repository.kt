package com.example.dayflow.data.repository

import com.example.dayflow.data.local.entity.DailyTaskEntity

interface Repository {

    suspend fun addTask(task: DailyTaskEntity)
    suspend fun getAllTasks(): List<DailyTaskEntity>
    suspend fun deleteTask(id: Int)
    suspend fun updateTaskStatus(id: Int, status: Boolean)
}