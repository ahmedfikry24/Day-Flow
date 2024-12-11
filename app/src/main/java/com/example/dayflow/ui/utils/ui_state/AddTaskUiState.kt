package com.example.dayflow.ui.utils.ui_state

import com.example.dayflow.data.local.entity.TaskEntity
import com.example.dayflow.ui.utils.convertDateToLong

private const val INITIAL_DATE = "0/0/0"

data class AddTaskUiState(
    val title: String = "",
    val titleError: Boolean = false,
    val description: String = "",
    val date: String = INITIAL_DATE,
    val time: String = INITIAL_DATE,
    val isAlarmDialogVisible: Boolean = false,
)

fun AddTaskUiState.toEntity(): TaskEntity {
    return TaskEntity(
        title = this.title,
        description = this.description,
        date = if (this.date == INITIAL_DATE) null else this.date.convertDateToLong(),
        status = false,
    )
}