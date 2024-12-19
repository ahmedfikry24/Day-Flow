package com.example.dayflow.ui.yearly_tasks.composable

import androidx.compose.foundation.layout.Arrangement
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
fun SwipeYearlyTask(
    modifier: Modifier = Modifier,
    state: TaskUiState,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainer,
    onSwipeDelete: (TaskUiState) -> Unit,
    onClickTask: (TaskUiState) -> Unit,
) {
    SwipeItemToDismiss(
        modifier = modifier.fillMaxWidth(),
        enableSwipeStartToEnd = false,
        onSwipeLeft = { onSwipeDelete(state) },
        backgroundContent = {
            SwipeTaskBackground(
                iconsRes = listOf(R.drawable.ic_trash),
                iconsArrangement = Arrangement.End
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