package com.example.dayflow.ui.yearly_tasks.vm

import com.example.dayflow.ui.utils.interaction.AddTaskInteraction

interface YearlyTasksInteractions : AddTaskInteraction {
    fun initData()
    fun controlAddTaskVisibility()
}
