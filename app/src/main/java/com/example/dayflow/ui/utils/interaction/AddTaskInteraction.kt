package com.example.dayflow.ui.utils.interaction

interface AddTaskInteraction {
    fun onTitleChange(title: String)
    fun onDescriptionChange(description: String)
    fun onDateChange(date: String)
    fun addTask()
}