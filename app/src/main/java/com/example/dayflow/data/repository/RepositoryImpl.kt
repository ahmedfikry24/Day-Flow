package com.example.dayflow.data.repository

import com.example.dayflow.data.local.LocalDataBase
import com.example.dayflow.data.local.entity.DailyTaskEntity
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val localDataBase: LocalDataBase,
) : Repository {

    override suspend fun addTask(task: DailyTaskEntity) {
        localDataBase.dailyTaskDao().addTask(task)
    }

    override suspend fun getAllTasks(): List<DailyTaskEntity> {
        return localDataBase.dailyTaskDao().getAllTasks()
    }

    override suspend fun deleteTask(id: Int) {
        localDataBase.dailyTaskDao().deleteTask(id)
    }

    override suspend fun updateTaskStatus(id: Int, status: Boolean) {
        localDataBase.dailyTaskDao().updateTaskStatus(id, status)
    }
}
