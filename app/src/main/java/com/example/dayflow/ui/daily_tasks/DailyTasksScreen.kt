package com.example.dayflow.ui.daily_tasks

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.dayflow.ui.composable.ErrorContent
import com.example.dayflow.ui.composable.LoadingContent
import com.example.dayflow.ui.composable.VisibleContent
import com.example.dayflow.ui.daily_tasks.vm.DailyTasksInteractions
import com.example.dayflow.ui.daily_tasks.vm.DailyTasksUiState
import com.example.dayflow.ui.daily_tasks.vm.DailyTasksViewModel
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
        LazyColumn(modifier = Modifier.fillMaxSize()) {

        }
    }
    ErrorContent(
        isVisible = state.contentStatus == ContentStatus.FAILURE,
        onTryAgain = {}
    )
}
