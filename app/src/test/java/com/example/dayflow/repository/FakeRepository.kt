package com.example.dayflow.repository

import com.example.dayflow.data.local.entity.BlockAppInfoEntity
import com.example.dayflow.data.local.entity.DailyTaskEntity
import com.example.dayflow.data.local.entity.YearlyTaskEntity
import com.example.dayflow.data.repository.Repository

class FakeRepository : Repository {

    private val dailyTasks = mutableListOf<DailyTaskEntity>()
    private val yearlyTasks = mutableListOf<YearlyTaskEntity>()
    private val blockedApps = mutableListOf<BlockAppInfoEntity>()

    override suspend fun addDailyTask(task: DailyTaskEntity) {
        dailyTasks.add(task)
    }

    override suspend fun getAllDailyTasks(): List<DailyTaskEntity> {
        return dailyTasks
    }

    override suspend fun deleteDailyTask(id: Int) {
        dailyTasks.filter { it.id == id }
    }

    override suspend fun updateDailyTaskStatus(id: Int, status: Boolean) {
        val taskIndex = dailyTasks.indexOfFirst { it.id == id }
        dailyTasks[taskIndex] = dailyTasks[taskIndex].copy(status = status)
    }

    override suspend fun addYearlyTask(task: YearlyTaskEntity) {
        yearlyTasks.add(task)
    }

    override suspend fun getAllYearlyTasks(): List<YearlyTaskEntity> {
        return yearlyTasks
    }

    override suspend fun deleteYearlyTask(id: Int) {
        yearlyTasks.filter { it.id == id }
    }

    override suspend fun getAllBlockedApps(): List<BlockAppInfoEntity> {
        return blockedApps
    }

    override suspend fun addBlockedApp(info: BlockAppInfoEntity) {
        blockedApps.add(info)
    }

    override suspend fun removeBlockedApp(id: Int) {
        blockedApps.filter { it.id == id }
    }
}