package com.example.dayflow.ui.composable

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.dayflow.R
import com.example.dayflow.ui.utils.ui_state.TaskUiState

@Composable
fun SwipeYearlyTask(
    modifier: Modifier = Modifier,
    state: TaskUiState,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainer,
    onSwipeDelete: (TaskUiState) -> Unit,
    onClickTask: (TaskUiState) -> Unit,
) {
    val swipeState = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            when (it) {
                SwipeToDismissBoxValue.StartToEnd -> return@rememberSwipeToDismissBoxState false
                SwipeToDismissBoxValue.EndToStart -> onSwipeDelete(state)
                SwipeToDismissBoxValue.Settled -> return@rememberSwipeToDismissBoxState false
            }
            return@rememberSwipeToDismissBoxState true
        },
        positionalThreshold = { it * .25f }
    )
    SwipeToDismissBox(
        modifier = modifier.fillMaxWidth(),
        state = swipeState,
        backgroundContent = {
            SwipeTaskBackground(
                state = swipeState.dismissDirection,
                iconsRes = listOf(R.drawable.ic_trash)
            )
        },
        gesturesEnabled = !state.isDone
    ) {
        TaskItem(
            state = state,
            containerColor = containerColor,
            onClick = onClickTask
        )
    }
}