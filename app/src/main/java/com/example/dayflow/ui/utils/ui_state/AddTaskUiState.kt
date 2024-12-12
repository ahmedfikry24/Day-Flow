package com.example.dayflow.ui.utils.ui_state

import com.example.dayflow.data.local.entity.TaskEntity
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
)

fun AddTaskUiState.toEntity(): TaskEntity {
    return TaskEntity(
        title = this.title,
        description = this.description,
        date = if (this.date == INITIAL_DATE) null else this.date.convertDateToLong(),
        time = if (this.title == INITIAL_TIME) null else this.time.convertTimeToLong(),
        status = false,
    )
}