package com.example.dayflow.ui.daily_tasks.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.dayflow.R
import com.example.dayflow.ui.composable.NoItemFound
import com.example.dayflow.ui.composable.PrimaryDialog
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
        Column(Modifier.fillMaxSize()) {
            NoItemFound(
                isVisible = state.inProgressTasks.isEmpty(),
                isButtonVisible = false
            )
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
                        onSwipeDone = { interactions.onSwipeDoneTask(it) },
                        onSwipeDelete = {
                            interactions.onSwipeDeleteTask(it)
                            interactions.controlDeleteItemDialogVisibility()
                        },
                        onClickTask = {}
                    )
                }
            }
        }
        AddDailyTask(state = state, interaction = interactions)
    }

    if (state.isDeleteTaskDialogVisible)
        PrimaryDialog(
            title = stringResource(R.string.warning),
            text = stringResource(R.string.are_you_sure_to_delete_this_item_we_cannot_get_it_back),
            confirmText = stringResource(R.string.delete),
            confirmContainerColor = MaterialTheme.colorScheme.error,
            confirmContentColor = MaterialTheme.colorScheme.onError,
            onConfirm = {
                interactions.deleteTask()
                interactions.controlDeleteItemDialogVisibility()
            },
            onCancel = interactions::controlDeleteItemDialogVisibility,
            onDismiss = interactions::controlDeleteItemDialogVisibility
        )
}
