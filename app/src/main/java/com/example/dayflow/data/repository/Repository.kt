package com.example.dayflow.data.repository

import com.example.dayflow.data.local.entity.TaskEntity

interface Repository {

    suspend fun addTask(task: TaskEntity)
    suspend fun getAllTasks(): List<TaskEntity>
    suspend fun deleteTask(id: Int)
    suspend fun updateTaskStatus(id: Int, status: Boolean)
}