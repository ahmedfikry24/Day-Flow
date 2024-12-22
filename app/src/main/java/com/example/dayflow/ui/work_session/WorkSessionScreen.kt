package com.example.dayflow.ui.work_session

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
import com.example.dayflow.ui.utils.ContentStatus
import com.example.dayflow.ui.utils.EventHandler
import com.example.dayflow.ui.work_session.vm.WorkSessionInteractions
import com.example.dayflow.ui.work_session.vm.WorkSessionUiState
import com.example.dayflow.ui.work_session.vm.WorkSessionViewModel

@Composable
fun WorkSessionScreen(
    navController: NavController,
    viewModel: WorkSessionViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    EventHandler(effects = viewModel.events) { event, _ ->

    }
    WorkSessionContent(state = state, interactions = viewModel)
}

@Composable
private fun WorkSessionContent(
    state: WorkSessionUiState,
    interactions: WorkSessionInteractions
) {
    LoadingContent(isVisible = state.contentStatus == ContentStatus.LOADING)
    VisibleContent(isVisible = state.contentStatus == ContentStatus.VISIBLE) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {

        }
    }
    ErrorContent(
        isVisible = state.contentStatus == ContentStatus.FAILURE,
        onTryAgain = interactions::initData
    )
}
