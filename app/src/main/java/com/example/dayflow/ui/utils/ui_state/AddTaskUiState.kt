package com.example.dayflow.ui.utils.ui_state

import com.example.dayflow.data.local.entity.DailyTaskEntity
import com.example.dayflow.data.local.entity.YearlyTaskEntity
import com.example.dayflow.ui.utils.UiConstants
import com.example.dayflow.ui.utils.convertDateToLong
import com.example.dayflow.ui.utils.convertTimeToLong

data class AddTaskUiState(
    val title: String = "",
    val titleError: Boolean = false,
    val description: String = "",
    val date: String = UiConstants.INITIAL_DATE,
    val time: String = UiConstants.INITIAL_TIME,
    val isSchedulingEmptyDialogVisibility: Boolean = false,
    val isScheduledUnValid: Boolean = false
)

fun AddTaskUiState.toDailyEntity(): DailyTaskEntity {
    return DailyTaskEntity(
        id = UiConstants.lastDailyTaskId,
        title = this.title,
        description = this.description,
        date = if (this.date == UiConstants.INITIAL_DATE) null else this.date.convertDateToLong(),
        time = if (this.time == UiConstants.INITIAL_TIME) null else this.time.convertTimeToLong(),
        status = false,
    )
}

fun AddTaskUiState.toUiState(id: Int): TaskUiState {
    return TaskUiState(
        id = id,
        title = this.title,
        description = this.description,
        date = this.date,
        time = this.time,
        isDone = false,
    )
}

fun AddTaskUiState.toYearlyEntity(): YearlyTaskEntity {
    return YearlyTaskEntity(
        id = UiConstants.lastYearlyTaskId,
        title = this.title,
        description = this.description,
    )
}