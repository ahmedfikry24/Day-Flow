package com.example.dayflow.data.repository

import com.example.dayflow.data.local.LocalDataBase
import com.example.dayflow.data.local.entity.TaskEntity
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val localDataBase: LocalDataBase,
) : Repository {

    override suspend fun addTask(task: TaskEntity) {
        localDataBase.taskDao().addTask(task)
    }

    override suspend fun getAllTasks(): List<TaskEntity> {
        return localDataBase.taskDao().getAllTasks()
    }

    override suspend fun deleteTask(id: Int) {
        localDataBase.taskDao().deleteTask(id)
    }

    override suspend fun updateTaskStatus(id: Int, status: Boolean) {
        localDataBase.taskDao().updateTaskStatus(id, status)
    }
}
