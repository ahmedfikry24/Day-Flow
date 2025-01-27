package com.example.dayflow.utils

import com.example.dayflow.data.local.entity.DailyTaskEntity
import com.example.dayflow.data.utils.TaskPriority
import com.example.dayflow.ui.utils.UiConstants
import com.example.dayflow.ui.utils.convertDateToLong
import com.example.dayflow.ui.utils.convertLongToDate
import com.example.dayflow.ui.utils.convertLongToTime
import com.example.dayflow.ui.utils.convertSessionTimeToLong
import com.example.dayflow.ui.utils.convertTimeToLong
import com.example.dayflow.ui.utils.formatSessionTime
import com.example.dayflow.ui.utils.formatTimeDigits
import com.example.dayflow.ui.utils.generateRandomId
import com.example.dayflow.ui.utils.getAlarmTime
import com.example.dayflow.ui.utils.getTaskPriority
import com.example.dayflow.ui.utils.validateRequireField
import org.junit.Assert
import org.junit.Test
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

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
        val date = 1689458400000L

        val expectedDate = "2023-07-16"
        val formatedDate = date.convertLongToDate()

        Assert.assertEquals(expectedDate, formatedDate)
    }

    @Test
    fun `given timestamp when convert long to date then return date string`() {
        val epochTimestamp = 0L
        val expectedDateString = Instant.ofEpochMilli(epochTimestamp)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

        val result = epochTimestamp.convertLongToDate()

        Assert.assertEquals(expectedDateString, result)
    }

    @Test
    fun `given valid timestamp when convert long to date then return correct date string`() {
        val time = LocalDate.parse("2025-01-27")
            .atStartOfDay(ZoneId.systemDefault())
            .toEpochSecond() * 1000
        val expectedDate = "2025-01-27"

        val formattedDate = time.convertLongToDate()

        Assert.assertEquals(expectedDate, formattedDate)
    }

    @Test
    fun `given string data type when format date string to long then correct date long`() {
        val date = "2023-07-16"
        val expectedDate = LocalDate.parse(date)
            .atStartOfDay(ZoneId.systemDefault())
            .toEpochSecond() * 1000

        val formatedDate = date.convertDateToLong()

        Assert.assertEquals(expectedDate, formatedDate)
    }

    @Test
    fun `given long time when format time to string then return valid format`() {
        val time = 60 * 60 * 1000L

        val formatter = Instant.ofEpochMilli(time)
            .atZone(ZoneId.systemDefault())
            .toLocalTime()
            .format(DateTimeFormatter.ofPattern("HH:mm"))
        val expectedTime = formatter.format(Date(time))

        val formatedTime = time.convertLongToTime()

        Assert.assertEquals(expectedTime, formatedTime)
    }

    @Test
    fun `given string time when convert time to long then return right value`() {
        val time = "16:30"

        val expectedTime = SimpleDateFormat("HH:mm", Locale.getDefault())
            .parse("16:30")?.time ?: 0L

        val formatedTime = time.convertTimeToLong()

        Assert.assertEquals(expectedTime, formatedTime)
    }

    @Test
    fun `given empty string time when convert time to long then return zero`() {
        val time = ""

        val formatedTime = time.convertTimeToLong()

        Assert.assertEquals(0L, formatedTime)
    }

    @Test
    fun `given time with single digits when format time digits then correct format`() {
        val time = "8:0"
        val expectedTime = "08:00"

        val formatedTime = time.formatTimeDigits()

        Assert.assertEquals(expectedTime, formatedTime)
    }

    @Test
    fun `given date and time when get alarm time then return right value`() {
        val time = "8:0"
        val date = "2025-01-27"

        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        val expectedTime = dateFormat.parse("$date $time")?.time ?: 0L

        val alarmTime = getAlarmTime(date, time)

        Assert.assertEquals(expectedTime, alarmTime)
    }

    @Test
    fun `given integer value when get random value then return true`() {
        val id = UiConstants.lastDailyTaskId

        val randomValue = generateRandomId()

        Assert.assertTrue(id <= randomValue)
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