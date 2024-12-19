package com.example.dayflow.ui.daily_tasks.vm

import com.example.dayflow.ui.utils.interaction.AddTaskInteraction
import com.example.dayflow.ui.utils.ui_state.TaskUiState

interface DailyTasksInteractions : AddTaskInteraction {
    fun initData()
    fun controlAddTaskVisibility()
    fun onSwipeDoneTask(task: TaskUiState)
    fun onSwipeDeleteTask(task: TaskUiState)
}
