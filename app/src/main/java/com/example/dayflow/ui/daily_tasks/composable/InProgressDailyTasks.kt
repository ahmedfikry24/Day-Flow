package com.example.dayflow.ui.daily_tasks.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.dayflow.ui.composable.SwipeDailyTask
import com.example.dayflow.ui.daily_tasks.vm.DailyTasksInteractions
import com.example.dayflow.ui.daily_tasks.vm.DailyTasksUiState
import com.example.dayflow.ui.theme.spacing

@Composable
fun InProgressDailyTasks(
    modifier: Modifier = Modifier,
    state: DailyTasksUiState,
    interactions: DailyTasksInteractions,
) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.space16),
        ) {
            itemsIndexed(
                items = state.inProgressTasks,
                key = { _, it -> it.id }
            ) { index, task ->
                val containerColor = listOf(
                    MaterialTheme.colorScheme.surfaceContainer,
                    MaterialTheme.colorScheme.primaryContainer,
                    MaterialTheme.colorScheme.secondaryContainer,
                    MaterialTheme.colorScheme.tertiaryContainer
                )
                SwipeDailyTask(
                    state = task,
                    containerColor = containerColor[index % containerColor.size],
                    onSwipeDone = {},
                    onSwipeDelete = {}
                )
            }
        }
        AddDailyTask(state = state, interaction = interactions)
    }
}
