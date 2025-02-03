package com.example.dayflow.data.local

import com.example.dayflow.data.local.entity.YearlyTaskEntity
import com.example.dayflow.utils.BaseAndroidTester
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.coVerify
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

@HiltAndroidTest
class YearlyTasksDao : BaseAndroidTester() {

    @Test
    fun given_EmptyDatabase_when_getAllTasks_then_returnEmptyList() = runTest {

        val tasks = repository.getAllYearlyTasks()

        assertTrue(tasks.isEmpty())
    }

    @Test
    fun given_taskEntity_when_addTask_then_addTaskSuccess() = runTest {
        val olderTasks = repository.getAllYearlyTasks()
        assertTrue(olderTasks.isEmpty())

        val task = YearlyTaskEntity(
            title = "task 1",
            description = "description",
        )

        repository.addYearlyTask(task)

        val newerTasks = repository.getAllYearlyTasks()

        assertEquals(1, newerTasks.size)
    }

    @Test
    fun given_taskId_when_deleteTask_then_removeTaskSuccess() = runTest {
        val task = YearlyTaskEntity(
            id = 1,
            title = "task 1",
            description = "description",
        )
        repository.addYearlyTask(task)

        val olderTasks = repository.getAllYearlyTasks()
        assertTrue(olderTasks.isNotEmpty())

        repository.deleteYearlyTask(task.id)

        val currentTasks = repository.getAllYearlyTasks()
        assertTrue(currentTasks.isEmpty())
    }
}