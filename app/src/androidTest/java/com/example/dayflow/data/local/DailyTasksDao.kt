package com.example.dayflow.data.local

import com.example.dayflow.data.local.entity.DailyTaskEntity
import com.example.dayflow.utils.BaseAndroidTester
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

@HiltAndroidTest
class DailyTasksDao : BaseAndroidTester() {

    @Test
    fun given_EmptyDatabase_when_getAllTasks_then_returnEmptyList() = runTest {

        val tasks = repository.getAllDailyTasks()

        assertTrue(tasks.isEmpty())
    }

    @Test
    fun given_taskEntity_when_addTask_then_addTaskSuccess() = runTest {
        val olderTasks = repository.getAllDailyTasks()

        assertTrue(olderTasks.isEmpty())

        val task = DailyTaskEntity(
            title = "task 1",
            description = "description",
            status = false
        )

        repository.addDailyTask(task)

        val newerTasks = repository.getAllDailyTasks()

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
        repository.addDailyTask(task)

        val olderTasks = repository.getAllDailyTasks()
        assertTrue(olderTasks.isNotEmpty())

        repository.deleteDailyTask(task.id)

        val currentTasks = repository.getAllDailyTasks()
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
        repository.addDailyTask(task)

        val olderTasks = repository.getAllDailyTasks()
        assertTrue(olderTasks.isNotEmpty())


        repository.updateDailyTaskStatus(task.id, true)

        val currentTasks = repository.getAllDailyTasks()
        assertTrue(currentTasks.last().status)
    }
}