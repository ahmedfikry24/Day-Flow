package com.example.dayflow.ui.daily_tasks.composable

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.example.dayflow.R
import com.example.dayflow.ui.add_task.AddTask
import com.example.dayflow.ui.theme.spacing
import com.example.dayflow.ui.utils.interaction.AddTaskInteraction
import com.example.dayflow.ui.utils.ui_state.AddTaskUiState

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun AddDailyTask(
    modifier: Modifier = Modifier,
    state: AddTaskUiState,
    interaction: AddTaskInteraction,
) {
    var isAddVisible by remember { mutableStateOf(false) }
    SharedTransitionLayout(modifier = modifier) {
        AnimatedContent(
            isAddVisible,
            label = "basic_transition"
        ) { targetState ->
            when (targetState) {
                true -> AddTask(
                    modifier = Modifier
                        .sharedBounds(
                            rememberSharedContentState(key = "bounds"),
                            animatedVisibilityScope = this@AnimatedContent,
                            resizeMode = SharedTransitionScope.ResizeMode.ScaleToBounds()
                        )
                        .background(MaterialTheme.colorScheme.background),
                    state = state,
                    interaction = interaction
                ) { isAddVisible = false }

                false -> {
                    FloatingActionButton(
                        modifier = Modifier
                            .sharedBounds(
                                rememberSharedContentState(key = "bounds"),
                                animatedVisibilityScope = this@AnimatedContent,
                                resizeMode = SharedTransitionScope.ResizeMode.ScaleToBounds()
                            )
                            .padding(MaterialTheme.spacing.space16),
                        shape = RoundedCornerShape(MaterialTheme.spacing.space8),
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        onClick = { isAddVisible = true }
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_add_task),
                            contentDescription = null,
                        )
                    }
                }
            }
        }
    }
}