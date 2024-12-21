package com.example.dayflow.ui.utils.ui_state

import com.example.dayflow.data.local.entity.DailyTaskEntity
import com.example.dayflow.data.local.entity.YearlyTaskEntity
import com.example.dayflow.ui.utils.UiConstants
import com.example.dayflow.ui.utils.convertDateToLong
import com.example.dayflow.ui.utils.convertLongToDate
import com.example.dayflow.ui.utils.convertLongToTime
import com.example.dayflow.ui.utils.convertTimeToLong

data class TaskUiState(
    val id: Int = 0,
    val title: String = "",
    val description: String = "",
    val date: String = UiConstants.INITIAL_DATE,
    val time: String = UiConstants.INITIAL_TIME,
    val isDone: Boolean = false,
)

fun DailyTaskEntity.toUiState(): TaskUiState {
    return TaskUiState(
        id = this.id,
        title = this.title,
        description = this.description,
        date = this.date?.convertLongToDate() ?: UiConstants.INITIAL_DATE,
        time = this.time?.convertLongToTime() ?: UiConstants.INITIAL_TIME,
        isDone = this.status
    )
}

fun TaskUiState.toDailyEntity(): DailyTaskEntity {
    return DailyTaskEntity(
        id = this.id,
        title = this.title,
        description = this.description,
        date = if (this.date == UiConstants.INITIAL_DATE) null else this.date.convertDateToLong(),
        time = if (this.time == UiConstants.INITIAL_TIME) null else this.time.convertTimeToLong(),
        status = false,
    )
}

fun YearlyTaskEntity.toUiState(): TaskUiState {
    return TaskUiState(
        id = this.id,
        title = this.title,
        description = this.description,
    )
}
