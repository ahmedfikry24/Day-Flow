package com.example.dayflow.ui.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.dayflow.R
import com.example.dayflow.ui.theme.spacing
import com.example.dayflow.ui.utils.applyPadding
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
    val swipeState = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            when (it) {
                SwipeToDismissBoxValue.StartToEnd -> onSwipeDone(state)
                SwipeToDismissBoxValue.EndToStart -> onSwipeDelete(state)
                SwipeToDismissBoxValue.Settled -> return@rememberSwipeToDismissBoxState false
            }
            return@rememberSwipeToDismissBoxState true
        },
        positionalThreshold = { it * .25f }
    )
    Box(modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        SwipeToDismissBox(
            modifier = Modifier
                .fillMaxWidth()
                .applyPadding(
                    state.isDone,
                    PaddingValues(horizontal = MaterialTheme.spacing.space8)
                ),
            state = swipeState,
            backgroundContent = {
                SwipeTaskBackground(
                    state = swipeState.dismissDirection,
                    iconsRes = listOf(R.drawable.ic_check_right, R.drawable.ic_trash)
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
        if (state.isDone)
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 2.dp,
                color = MaterialTheme.colorScheme.onBackground
            )
    }
}