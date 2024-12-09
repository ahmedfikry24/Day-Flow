package com.example.dayflow.ui.daily_tasks.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.dayflow.ui.composable.TaskItem
import com.example.dayflow.ui.daily_tasks.vm.DailyTasksUiState
import com.example.dayflow.ui.theme.spacing
import com.example.dayflow.ui.utils.ui_state.TaskUiState

@Composable
fun DoneDailyTasks(
    modifier: Modifier = Modifier,
    state: DailyTasksUiState
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.space16),
    ) {
        items(
            items = state.doneTasks,
            key = { it.id }
        ) { task ->
            TaskItem(state = task)
        }
    }

}
