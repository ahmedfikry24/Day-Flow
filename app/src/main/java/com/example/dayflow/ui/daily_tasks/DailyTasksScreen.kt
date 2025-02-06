package com.example.dayflow.ui.daily_tasks

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.dayflow.ui.composable.ErrorContent
import com.example.dayflow.ui.composable.LoadingContent
import com.example.dayflow.ui.composable.VisibleContent
import com.example.dayflow.ui.daily_tasks.composable.DailyTaskTabs
import com.example.dayflow.ui.daily_tasks.composable.DoneDailyTasks
import com.example.dayflow.ui.daily_tasks.composable.InProgressDailyTasks
import com.example.dayflow.ui.daily_tasks.composable.RememberNotificationPermission
import com.example.dayflow.ui.daily_tasks.vm.DailyTasksInteractions
import com.example.dayflow.ui.daily_tasks.vm.DailyTasksUiState
import com.example.dayflow.ui.daily_tasks.vm.DailyTasksViewModel
import com.example.dayflow.ui.theme.spacing
import com.example.dayflow.ui.utils.ContentStatus
import com.example.dayflow.ui.utils.EventHandler
import com.example.dayflow.ui.utils.UiTestTags

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
    LoadingContent(
        modifier = Modifier.testTag(UiTestTags.LOADING_CONTENT),
        isVisible = state.contentStatus == ContentStatus.LOADING
    )
    VisibleContent(
        modifier = Modifier.testTag(UiTestTags.VISIBLE_CONTENT),
        isVisible = state.contentStatus == ContentStatus.VISIBLE
    ) {
        RememberNotificationPermission()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(MaterialTheme.spacing.space16),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.space16)
        ) {
            var isDoneVisible by remember { mutableStateOf(false) }
            DailyTaskTabs(
                modifier = Modifier.testTag(UiTestTags.DAILY_TABS),
                isTabsVisible = !state.isAddTaskVisible,
                isDoneVisible = isDoneVisible,
                onClickDone = { isDoneVisible = true },
                onClickInProgress = { isDoneVisible = false }
            )
            Crossfade(
                modifier = Modifier.weight(1f),
                targetState = isDoneVisible,
                label = "transition"
            ) { targetValue ->
                when (targetValue) {
                    true -> DoneDailyTasks(
                        modifier = Modifier.testTag(UiTestTags.DONE_DAILY_TASKS_CONTENT),
                        state = state
                    )

                    false -> InProgressDailyTasks(
                        modifier = Modifier.testTag(UiTestTags.IN_PROGRESS_DAILY_TASKS_CONTENT),
                        state = state, interactions = interactions
                    )
                }
            }
        }
    }
    ErrorContent(
        modifier = Modifier.testTag(UiTestTags.FAILURE_CONTENT),
        isVisible = state.contentStatus == ContentStatus.FAILURE,
        onTryAgain = interactions::initData
    )
}
