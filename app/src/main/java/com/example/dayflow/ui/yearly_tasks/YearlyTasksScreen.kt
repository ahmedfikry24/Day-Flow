package com.example.dayflow.ui.yearly_tasks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.dayflow.ui.composable.ErrorContent
import com.example.dayflow.ui.composable.LoadingContent
import com.example.dayflow.ui.composable.VisibleContent
import com.example.dayflow.ui.theme.spacing
import com.example.dayflow.ui.utils.ContentStatus
import com.example.dayflow.ui.utils.EventHandler
import com.example.dayflow.ui.yearly_tasks.composable.SwipeYearlyTask
import com.example.dayflow.ui.yearly_tasks.vm.YearlyTasksInteractions
import com.example.dayflow.ui.yearly_tasks.vm.YearlyTasksUiState
import com.example.dayflow.ui.yearly_tasks.vm.YearlyTasksViewModel

@Composable
fun YearlyTasksScreen(
    navController: NavController,
    viewModel: YearlyTasksViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    EventHandler(effects = viewModel.events) { event, _ ->

    }
    YearlyTasksContent(state = state, interactions = viewModel)
}

@Composable
private fun YearlyTasksContent(
    state: YearlyTasksUiState,
    interactions: YearlyTasksInteractions
) {
    LoadingContent(isVisible = state.contentStatus == ContentStatus.LOADING)
    VisibleContent(isVisible = state.contentStatus == ContentStatus.VISIBLE) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(MaterialTheme.spacing.space16),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.space16)
        ) {
            items(state.tasks) { task ->
                SwipeYearlyTask(
                    state = task,
                    onSwipeDelete = {},
                    onClickTask = {}
                )
            }
        }
    }
    ErrorContent(
        isVisible = state.contentStatus == ContentStatus.FAILURE,
        onTryAgain = interactions::initData
    )
}
