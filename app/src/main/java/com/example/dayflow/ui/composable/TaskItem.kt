package com.example.dayflow.ui.composable

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.dayflow.ui.theme.spacing
import com.example.dayflow.ui.utils.applyPadding
import com.example.dayflow.ui.utils.ui_state.TaskUiState

@Composable
fun TaskItem(
    modifier: Modifier = Modifier,
    state: TaskUiState,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainer,
    onSwipeDone: (TaskUiState) -> Unit,
    onSwipeDelete: (TaskUiState) -> Unit,
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
            backgroundContent = { SwipeTaskBackground(state = swipeState.dismissDirection) },
            gesturesEnabled = !state.isDone
        ) {
            TaskContent(
                state = state,
                containerColor = containerColor
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

@Composable
private fun TaskContent(
    modifier: Modifier = Modifier,
    state: TaskUiState,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainer,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(MaterialTheme.spacing.space4),
        color = containerColor,
        border = BorderStroke(1.dp, color = MaterialTheme.colorScheme.outlineVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.spacing.space8),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.space8)
            ) {
                Text(
                    state.title,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    state.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            if (state.date.isNotBlank())
                Text(
                    state.date,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline,
                )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TaskItemPreview() {
    TaskItem(
        state = TaskUiState(),
        onSwipeDone = {},
        onSwipeDelete = {}
    )
}