package com.example.dayflow.ui.daily_tasks.vm

import com.example.dayflow.ui.utils.ContentStatus
import com.example.dayflow.ui.utils.ui_state.AddTaskUiState
import com.example.dayflow.ui.utils.ui_state.TaskUiState

data class DailyTasksUiState(
    val contentStatus: ContentStatus = ContentStatus.LOADING,
    val doneTasks: List<TaskUiState> = listOf(),
    val inProgressTasks: List<TaskUiState> = listOf(),
    val isAddTaskVisible: Boolean = false,
    val addTask: AddTaskUiState = AddTaskUiState(),
) 
