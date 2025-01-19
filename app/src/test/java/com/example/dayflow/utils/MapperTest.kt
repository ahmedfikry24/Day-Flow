package com.example.dayflow.utils

import com.example.dayflow.data.local.entity.DailyTaskEntity
import com.example.dayflow.ui.utils.UiConstants
import com.example.dayflow.ui.utils.ui_state.TaskUiState
import com.example.dayflow.ui.utils.ui_state.toDailyEntity
import com.example.dayflow.ui.utils.ui_state.toUiState
import org.junit.Assert.assertEquals
import org.junit.Test


class MapperTest {
    
    @Test
    fun `given daily ui state with initial date when convert to room entity then set date to null`() {
        val task = TaskUiState(
            id = 0,
            title = "ahmed",
            description = "",
            date = UiConstants.INITIAL_DATE,
        )
        val createdAt = System.currentTimeMillis()
        val expectedTask = DailyTaskEntity(
            id = 0,
            title = "ahmed",
            description = "",
            date = null,
            createdAt = createdAt,
            status = false
        )

        val actualTask = task.toDailyEntity().copy(createdAt = createdAt)
        assertEquals(expectedTask, actualTask)
    }


    @Test
    fun `given daily ui state with initial time when convert to room entity then set time to null`() {
        val task = TaskUiState(
            id = 0,
            title = "ahmed",
            description = "",
            time = UiConstants.INITIAL_TIME,
        )
        val createdAt = System.currentTimeMillis()
        val expectedTask = DailyTaskEntity(
            id = 0,
            title = "ahmed",
            description = "",
            time = null,
            createdAt = createdAt,
            status = false
        )

        val actualTask = task.toDailyEntity().copy(createdAt = createdAt)
        assertEquals(expectedTask, actualTask)
    }

    @Test
    fun `given daily entity with null date when convert to ui state then set date to initial value`() {
        val task = DailyTaskEntity(
            id = 0,
            title = "ahmed",
            description = "",
            date = null,
            time = null,
            status = false
        )

        val expectedTask = TaskUiState(
            id = 0,
            title = "ahmed",
            description = "",
            date = UiConstants.INITIAL_DATE,
            isDone = false
        )

        val actualTask = task.toUiState()
        assertEquals(expectedTask, actualTask)
    }

    @Test
    fun `given daily entity with null time when convert to ui state then set time to initial value`() {
        val task = DailyTaskEntity(
            id = 0,
            title = "ahmed",
            description = "",
            date = null,
            time = null,
            status = false
        )

        val expectedTask = TaskUiState(
            id = 0,
            title = "ahmed",
            description = "",
            time = UiConstants.INITIAL_TIME,
            isDone = false
        )

        val actualTask = task.toUiState()
        assertEquals(expectedTask, actualTask)
    }
}