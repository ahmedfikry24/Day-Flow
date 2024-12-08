package com.example.dayflow.ui.daily_tasks.vm

import com.example.dayflow.ui.utils.ContentStatus
import com.example.dayflow.ui.utils.ui_state.TaskUiState

data class DailyTasksUiState(
    val contentStatus: ContentStatus = ContentStatus.LOADING,
    val tasks: List<TaskUiState> = listOf(),
) 
