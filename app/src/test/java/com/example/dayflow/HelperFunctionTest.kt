package com.example.dayflow

import com.example.dayflow.data.local.entity.DailyTaskEntity
import com.example.dayflow.data.utils.TaskPriority
import com.example.dayflow.ui.utils.convertDateToLong
import com.example.dayflow.ui.utils.convertLongToDate
import com.example.dayflow.ui.utils.convertLongToTime
import com.example.dayflow.ui.utils.convertSessionTimeToLong
import com.example.dayflow.ui.utils.convertTimeToLong
import com.example.dayflow.ui.utils.formatSessionTime
import com.example.dayflow.ui.utils.generateRandomId
import com.example.dayflow.ui.utils.getAlarmTime
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
    fun `given task list with valid date when sorting then return list sorted descending in order of week`() {
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

    @Test
    fun `given long data type when format date long to string then return valid format`() {
        val currentDate = 1689458400000L
        val dateString = "2023-07-16"
        val formatedDate = currentDate.convertLongToDate()
        Assert.assertEquals(dateString, formatedDate)
    }


    @Test
    fun `given string data type when format date string to long then return right value`() {
        val currentDate = "2023-07-16"
        val dateLong = 1689454800000L
        val formatedDate = currentDate.convertDateToLong()
        Assert.assertEquals(dateLong, formatedDate)
    }

    @Test
    fun `given long data type when format time long to string then return valid format`() {
        val currentTime = 52200000L
        val timeString = "16:30"
        val formatedTime = currentTime.convertLongToTime()
        Assert.assertEquals(timeString, formatedTime)
    }

    @Test
    fun `given string data type when format time string to long then return right value`() {
        val currentTime = "16:30"
        val timeLong = 52200000L
        val formatedTime = currentTime.convertTimeToLong()
        Assert.assertEquals(timeLong, formatedTime)
    }

    @Test
    fun `given date and time when get alarm time then return right value`() {
        val time = "16:30"
        val date = "2023-07-16"
        val alarmTime = getAlarmTime(date, time)
        val expectedTime = 1689514200000L
        Assert.assertEquals(expectedTime, alarmTime)
    }

    @Test
    fun `given integer value when get random value then return true`() {
        val randomValue = generateRandomId()
        Assert.assertTrue(randomValue in 1..Int.MAX_VALUE)
    }

    @Test
    fun `given integer value when get session time then return right value`() {
        val value = 30
        val expectedValue = 1800000L
        Assert.assertEquals(expectedValue, value.convertSessionTimeToLong())
    }

    @Test
    fun `given long value when convert session time then return valid format`() {
        val time = 1800000L
        val expectedFormat = "00:30:00"
        val formattedValue = time.formatSessionTime()
        Assert.assertEquals(expectedFormat, formattedValue)
    }
}