package com.example.dayflow.ui.utils.ui_state

import com.example.dayflow.data.local.entity.TaskEntity
import com.example.dayflow.ui.utils.convertLongToDate

data class TaskUiState(
    val id: Int = 0,
    val title: String = "",
    val description: String = "",
    val date: String = "",
    val time: String = "",
    val isDone: Boolean = false,
)

fun TaskEntity.toUiState(): TaskUiState {
    return TaskUiState(
        id = this.id,
        title = this.title,
        description = this.description,
        date = this.date?.convertLongToDate() ?: "",
        isDone = this.status
    )
}

