package com.example.dayflow.ui.daily_tasks.vm

import com.example.dayflow.ui.utils.interaction.AddTaskInteraction

interface DailyTasksInteractions : AddTaskInteraction {
    fun initData()
    fun controlAddTaskVisibility()
    fun onSwipeDoneTask(id: Int)
    fun onSwipeDeleteTask(id: Int)
}
