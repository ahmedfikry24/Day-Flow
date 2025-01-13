package com.example.dayflow.ui.yearly_tasks.composable

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.example.dayflow.R
import com.example.dayflow.ui.add_task.AddTask
import com.example.dayflow.ui.theme.spacing
import com.example.dayflow.ui.yearly_tasks.vm.YearlyTasksInteractions
import com.example.dayflow.ui.yearly_tasks.vm.YearlyTasksUiState

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun AddYearlyTask(
    modifier: Modifier = Modifier,
    state: YearlyTasksUiState,
    interaction: YearlyTasksInteractions,
) {
    SharedTransitionLayout(modifier = modifier) {
        AnimatedContent(
            state.isAddTaskVisible,
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
                    state = state.addTask,
                    isScheduleRequire = false,
                    interaction = interaction,
                    onCancel = interaction::controlAddTaskVisibility
                )

                false -> {
                    FloatingActionButton(
                        modifier = Modifier
                            .sharedBounds(
                                rememberSharedContentState(key = "bounds"),
                                animatedVisibilityScope = this@AnimatedContent,
                                resizeMode = SharedTransitionScope.ResizeMode.ScaleToBounds()
                            ),
                        shape = RoundedCornerShape(MaterialTheme.spacing.space8),
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        onClick = interaction::controlAddTaskVisibility
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