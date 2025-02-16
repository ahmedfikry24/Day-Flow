package com.example.dayflow.ui.work_session

import androidx.compose.animation.Crossfade
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.dayflow.ui.composable.LoadingContent
import com.example.dayflow.ui.composable.VisibleContent
import com.example.dayflow.ui.utils.ContentStatus
import com.example.dayflow.ui.utils.UiTestTags
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
    LoadingContent(
        modifier = Modifier.testTag(UiTestTags.LOADING_CONTENT),
        isVisible = state.contentStatus == ContentStatus.LOADING
    )
    VisibleContent(
        modifier = Modifier.testTag(UiTestTags.VISIBLE_CONTENT),
        isVisible = state.contentStatus == ContentStatus.VISIBLE
    ) {
        Crossfade(
            targetState = state.isSessionInfoVisible,
            label = "contentTransition"
        ) { targetState ->
            when (targetState) {
                true -> SessionInfo(
                    modifier = Modifier.testTag(UiTestTags.SESSION_INFO),
                    state = state,
                    interactions = interactions
                )

                false -> SessionCountDown(
                    modifier = Modifier.testTag(UiTestTags.SESSION_COUNT_DOWN),
                    state = state,
                    interactions = interactions
                )
            }
        }
    }
}
