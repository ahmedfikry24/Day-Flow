package com.example.dayflow.ui.yearly_tasks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.dayflow.R
import com.example.dayflow.ui.composable.ErrorContent
import com.example.dayflow.ui.composable.LoadingContent
import com.example.dayflow.ui.composable.NoItemFound
import com.example.dayflow.ui.composable.PrimaryDialog
import com.example.dayflow.ui.composable.VisibleContent
import com.example.dayflow.ui.theme.spacing
import com.example.dayflow.ui.utils.ContentStatus
import com.example.dayflow.ui.utils.EventHandler
import com.example.dayflow.ui.yearly_tasks.composable.AddYearlyTask
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(MaterialTheme.spacing.space16),
            contentAlignment = Alignment.BottomEnd
        ) {
            Column(Modifier.fillMaxSize()) {
                NoItemFound(
                    isVisible = state.tasks.isEmpty(),
                    isButtonVisible = false
                )
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = MaterialTheme.spacing.space16),
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.space16)
                ) {
                    items(
                        items = state.tasks,
                        key = { it.id }
                    ) { task ->
                        SwipeYearlyTask(
                            modifier = Modifier.animateItem(),
                            state = task,
                            onSwipeDelete = {
                                interactions.onSwipeDeleteTask(it.id)
                                interactions.controlDeleteItemDialogVisibility()
                            },
                            onClickTask = {}
                        )
                    }
                }
            }

            AddYearlyTask(state = state, interaction = interactions)
        }

        if (state.isDeleteTaskDialogVisible)
            PrimaryDialog(
                title = stringResource(R.string.warning),
                text = stringResource(R.string.are_you_sure_to_delete_this_item_we_cannot_get_it_back),
                confirmText = stringResource(R.string.delete),
                confirmContainerColor = MaterialTheme.colorScheme.error,
                confirmContentColor = MaterialTheme.colorScheme.onError,
                onConfirm = {
                    interactions.deleteTask()
                    interactions.controlDeleteItemDialogVisibility()
                },
                onCancel = interactions::controlDeleteItemDialogVisibility,
                onDismiss = interactions::controlDeleteItemDialogVisibility
            )
    }
    ErrorContent(
        isVisible = state.contentStatus == ContentStatus.FAILURE,
        onTryAgain = interactions::initData
    )
}
