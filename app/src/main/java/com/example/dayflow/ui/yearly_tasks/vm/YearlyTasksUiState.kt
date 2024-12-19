package com.example.dayflow.ui.yearly_tasks.vm

import com.example.dayflow.ui.utils.ContentStatus
import com.example.dayflow.ui.utils.ui_state.AddTaskUiState
import com.example.dayflow.ui.utils.ui_state.TaskUiState

data class YearlyTasksUiState(
    val contentStatus: ContentStatus = ContentStatus.LOADING,
    val tasks: List<TaskUiState> = listOf(),
    val isDeleteTaskDialogVisible: Boolean = false,
    val isAddTaskVisible: Boolean = false,
    val addTask: AddTaskUiState = AddTaskUiState(),
    val selectedDeleteItemId: Int = -1,
) 
