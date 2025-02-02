package com.example.dayflow.data.local

import com.example.dayflow.data.local.dao.DailyTaskDao
import com.example.dayflow.data.local.entity.DailyTaskEntity
import com.example.dayflow.utils.BaseAndroidTester
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test


class DailyTasksDao : BaseAndroidTester() {

    private lateinit var dailyTaskDao: DailyTaskDao

    override fun setUp() {
        super.setUp()
        dailyTaskDao = roomDatabase.dailyTaskDao()
    }

    @Test
    fun given_EmptyDatabase_when_getAllTasks_then_returnEmptyList() = runTest {

        val tasks = dailyTaskDao.getAllTasks()

        assertTrue(tasks.isEmpty())
    }

    @Test
    fun given_taskEntity_when_addTask_then_addTaskSuccess() = runTest {
        val olderTasks = dailyTaskDao.getAllTasks()

        assertTrue(olderTasks.isEmpty())

        val task = DailyTaskEntity(
            title = "task 1",
            description = "description",
            status = false
        )

        dailyTaskDao.addTask(task)

        val newerTasks = dailyTaskDao.getAllTasks()

        assertEquals(1, newerTasks.size)
    }

    @Test
    fun given_taskId_when_deleteTask_then_removeTaskSuccess() = runTest {
        val task = DailyTaskEntity(
            id = 1,
            title = "task 1",
            description = "description",
            status = false
        )
        dailyTaskDao.addTask(task)

        val olderTasks = dailyTaskDao.getAllTasks()
        assertTrue(olderTasks.isNotEmpty())

        dailyTaskDao.deleteTask(task.id)

        val currentTasks = dailyTaskDao.getAllTasks()
        assertTrue(currentTasks.isEmpty())
    }


    @Test
    fun given_taskId_when_updateTaskStatus_then_updateTaskSuccess() = runTest {
        val task = DailyTaskEntity(
            id = 1,
            title = "task 1",
            description = "description",
            status = false
        )
        dailyTaskDao.addTask(task)

        val olderTasks = dailyTaskDao.getAllTasks()
        assertTrue(olderTasks.isNotEmpty())


        dailyTaskDao.updateTaskStatus(task.id, true)

        val currentTasks = dailyTaskDao.getAllTasks()
        assertTrue(currentTasks.last().status)
    }
}