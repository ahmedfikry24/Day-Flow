package com.example.dayflow.data.local

import com.example.dayflow.data.local.dao.YearlyTaskDao
import com.example.dayflow.data.local.entity.YearlyTaskEntity
import com.example.dayflow.utils.BaseAndroidTester
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test


class YearlyTasksDao : BaseAndroidTester() {

    private lateinit var yearlyTaskDao: YearlyTaskDao

    override fun setUp() {
        super.setUp()
        yearlyTaskDao = roomDatabase.yearlyTaskDao()
    }

    @Test
    fun given_EmptyDatabase_when_getAllTasks_then_returnEmptyList() = runTest {

        val tasks = yearlyTaskDao.getAllTasks()

        assertTrue(tasks.isEmpty())
    }

    @Test
    fun given_taskEntity_when_addTask_then_addTaskSuccess() = runTest {
        val olderTasks = yearlyTaskDao.getAllTasks()

        assertTrue(olderTasks.isEmpty())

        val task = YearlyTaskEntity(
            title = "task 1",
            description = "description",
        )

        yearlyTaskDao.addTask(task)

        val newerTasks = yearlyTaskDao.getAllTasks()

        assertEquals(1, newerTasks.size)
    }

    @Test
    fun given_taskId_when_deleteTask_then_removeTaskSuccess() = runTest {
        val task = YearlyTaskEntity(
            id = 1,
            title = "task 1",
            description = "description",
        )
        yearlyTaskDao.addTask(task)

        val olderTasks = yearlyTaskDao.getAllTasks()
        assertTrue(olderTasks.isNotEmpty())

        yearlyTaskDao.deleteTask(task.id)

        val currentTasks = yearlyTaskDao.getAllTasks()
        assertTrue(currentTasks.isEmpty())
    }
}