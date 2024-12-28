package com.example.dayflow.ui.work_session

import androidx.compose.animation.Crossfade
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.dayflow.ui.composable.LoadingContent
import com.example.dayflow.ui.composable.VisibleContent
import com.example.dayflow.ui.utils.ContentStatus
import com.example.dayflow.ui.work_session.composable.SessionCountDown
import com.example.dayflow.ui.work_session.composable.SessionInfo
import com.example.dayflow.ui.work_session.vm.WorkSessionInteractions
import com.example.dayflow.ui.work_session.vm.WorkSessionUiState
import com.example.dayflow.ui.work_session.vm.WorkSessionViewModel

@Composable
fun WorkSessionScreen(viewModel: WorkSessionViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    WorkSessionContent(state = state, interactions = viewModel)
}

@Composable
private fun WorkSessionContent(
    state: WorkSessionUiState,
    interactions: WorkSessionInteractions
) {
    LoadingContent(isVisible = state.contentStatus == ContentStatus.LOADING)
    VisibleContent(isVisible = state.contentStatus == ContentStatus.VISIBLE) {
        Crossfade(
            targetState = state.isSessionInfoVisible,
            label = "contentTransition"
        ) { targetState ->
            when (targetState) {
                true -> SessionInfo(state = state, interactions = interactions)
                false -> SessionCountDown(state = state, interactions = interactions)
            }
        }
    }
}
