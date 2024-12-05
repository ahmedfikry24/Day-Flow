package com.example.dayflow.ui.daily_tasks.vm

import com.example.dayflow.ui.utils.ContentStatus

data class DailyTasksUiState(
    val contentStatus: ContentStatus = ContentStatus.LOADING,
) 
