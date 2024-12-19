package com.example.dayflow.ui.utils.ui_state

import com.example.dayflow.data.local.entity.DailyTaskEntity
import com.example.dayflow.data.local.entity.YearlyTaskEntity
import com.example.dayflow.ui.utils.convertDateToLong
import com.example.dayflow.ui.utils.convertTimeToLong

const val INITIAL_DATE = "0/0/0"
const val INITIAL_TIME = "0:0"

data class AddTaskUiState(
    val title: String = "",
    val titleError: Boolean = false,
    val description: String = "",
    val date: String = INITIAL_DATE,
    val time: String = INITIAL_TIME,
    val canScheduleAlarmDialogVisibility: Boolean = false,
    val isSchedulingEmptyDialogVisibility: Boolean = false,
    val isScheduledUnValid: Boolean = false
)

fun AddTaskUiState.toDailyEntity(): DailyTaskEntity {
    return DailyTaskEntity(
        title = this.title,
        description = this.description,
        date = if (this.date == INITIAL_DATE) null else this.date.convertDateToLong(),
        time = if (this.time == INITIAL_TIME) null else this.time.convertTimeToLong(),
        status = false,
    )
}

fun AddTaskUiState.toUiState(): TaskUiState {
    return TaskUiState(
        title = this.title,
        description = this.description,
        date = this.date,
        time = this.time,
        isDone = false,
    )
}

fun AddTaskUiState.toYearlyEntity(): YearlyTaskEntity {
    return YearlyTaskEntity(
        title = this.title,
        description = this.description,
    )
}