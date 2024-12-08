package com.example.dayflow.ui.utils.ui_state

data class AddTaskUiState(
    val title: String = "",
    val titleError: Boolean = false,
    val description: String = "",
    val date: String = "0/0/0",
)
