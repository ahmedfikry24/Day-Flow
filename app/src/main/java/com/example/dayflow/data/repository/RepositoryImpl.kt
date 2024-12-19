package com.example.dayflow.data.repository

import com.example.dayflow.data.local.LocalDataBase
import com.example.dayflow.data.local.entity.DailyTaskEntity
import com.example.dayflow.data.local.entity.YearlyTaskEntity
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val localDataBase: LocalDataBase,
) : Repository {

    override suspend fun addDailyTask(task: DailyTaskEntity) {
        localDataBase.dailyTaskDao().addTask(task)
    }

    override suspend fun getAllDailyTasks(): List<DailyTaskEntity> {
        return localDataBase.dailyTaskDao().getAllTasks()
    }

    override suspend fun deleteDailyTask(id: Int) {
        localDataBase.dailyTaskDao().deleteTask(id)
    }

    override suspend fun updateDailyTaskStatus(id: Int, status: Boolean) {
        localDataBase.dailyTaskDao().updateTaskStatus(id, status)
    }

    override suspend fun addYearlyTask(task: YearlyTaskEntity) {
        localDataBase.yearlyTaskDao().addTask(task)
    }

    override suspend fun getAllYearlyTasks(): List<YearlyTaskEntity> {
        return localDataBase.yearlyTaskDao().getAllTasks()
    }

    override suspend fun deleteYearlyTask(id: Int) {
        localDataBase.yearlyTaskDao().deleteTask(id)
    }
}
