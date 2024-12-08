package com.example.dayflow.ui.daily_tasks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.dayflow.ui.composable.ErrorContent
import com.example.dayflow.ui.composable.LoadingContent
import com.example.dayflow.ui.composable.SwipeDailyTask
import com.example.dayflow.ui.composable.VisibleContent
import com.example.dayflow.ui.daily_tasks.composable.AddDailyTask
import com.example.dayflow.ui.daily_tasks.vm.DailyTasksInteractions
import com.example.dayflow.ui.daily_tasks.vm.DailyTasksUiState
import com.example.dayflow.ui.daily_tasks.vm.DailyTasksViewModel
import com.example.dayflow.ui.theme.spacing
import com.example.dayflow.ui.utils.ContentStatus
import com.example.dayflow.ui.utils.EventHandler

@Composable
fun DailyTasksScreen(
    navController: NavController,
    viewModel: DailyTasksViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    EventHandler(effects = viewModel.events) { event, _ ->

    }
    DailyTasksContent(state = state, interactions = viewModel)
}

@Composable
private fun DailyTasksContent(
    state: DailyTasksUiState,
    interactions: DailyTasksInteractions,
) {
    LoadingContent(isVisible = state.contentStatus == ContentStatus.LOADING)
    VisibleContent(isVisible = state.contentStatus == ContentStatus.VISIBLE) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(MaterialTheme.spacing.space16),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.space16),
            ) {
                itemsIndexed(
                    items = state.tasks,
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
            AddDailyTask(state = state.addTask, interaction = interactions)
        }
    }
    ErrorContent(
        isVisible = state.contentStatus == ContentStatus.FAILURE,
        onTryAgain = interactions::initData
    )
}
