package com.example.dayflow

import com.example.dayflow.data.local.entity.DailyTaskEntity
import com.example.dayflow.data.utils.TaskPriority
import com.example.dayflow.ui.utils.getTaskPriority
import com.example.dayflow.ui.utils.validateRequireField
import org.junit.Assert
import org.junit.Test
import java.time.LocalDate

class HelperFunctionTest {

    @Test
    fun `given empty string when validate require field then return false`() {
        val field = ""
        Assert.assertFalse(field.validateRequireField())
    }

    @Test
    fun `given valid string when validate require field then return false`() {
        val field = "ahmed"
        Assert.assertTrue(field.validateRequireField())
    }

    @Test
    fun `given daily task with null date when get priority then return normal`() {
        val task = DailyTaskEntity(
            id = 0,
            title = "",
            description = "",
            date = null,
            time = null,
            status = false
        )
        val priority = task.date.getTaskPriority()
        Assert.assertEquals(TaskPriority.NORMAl, priority)
    }

    @Test
    fun `given task lit with valid date when sorting then return list sorted descending in order of week`() {
        val unSortedTasks = listOf(
            DailyTaskEntity(
                id = 0,
                title = "",
                description = "",
                date = LocalDate.now().plusDays(1).toEpochDay(),
                time = null,
                status = false
            ),
            DailyTaskEntity(
                id = 1,
                title = "",
                description = "",
                date = LocalDate.now().plusDays(2).toEpochDay(),
                time = null,
                status = false
            ),
            DailyTaskEntity(
                id = 2,
                title = "",
                description = "",
                date = LocalDate.now().plusDays(3).toEpochDay(),
                time = null,
                status = false
            )
        )
        val sortedTasks = unSortedTasks.sortedByDescending {
            when (it.date.getTaskPriority()) {
                TaskPriority.HIGH -> 2
                TaskPriority.NORMAl -> 1
            }
        }
        Assert.assertEquals(sortedTasks, unSortedTasks)
    }

}