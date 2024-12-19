package com.example.dayflow.ui.daily_tasks.composable

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.dayflow.R
import com.example.dayflow.ui.composable.SwipeItemToDismiss
import com.example.dayflow.ui.composable.SwipeTaskBackground
import com.example.dayflow.ui.composable.TaskItem
import com.example.dayflow.ui.utils.ui_state.TaskUiState

@Composable
fun SwipeDailyTask(
    modifier: Modifier = Modifier,
    state: TaskUiState,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainer,
    onSwipeDone: (TaskUiState) -> Unit,
    onSwipeDelete: (TaskUiState) -> Unit,
    onClickTask: (TaskUiState) -> Unit,
) {
    SwipeItemToDismiss(
        modifier = modifier.fillMaxWidth(),
        enableGesture = !state.isDone,
        onSwipeLeft = { onSwipeDelete(state) },
        onSwipeRight = { onSwipeDone(state) },
        backgroundContent = {
            SwipeTaskBackground(
                iconsRes = listOf(
                    R.drawable.ic_check_right,
                    R.drawable.ic_trash
                )
            )
        },
    ) {
        TaskItem(
            state = state,
            containerColor = containerColor,
            onClick = onClickTask
        )
    }
}