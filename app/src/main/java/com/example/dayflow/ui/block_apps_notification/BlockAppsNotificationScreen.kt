package com.example.dayflow.ui.block_apps_notification

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.dayflow.R
import com.example.dayflow.ui.block_apps_notification.composable.BlockAppInfoItem
import com.example.dayflow.ui.block_apps_notification.vm.BlockAppsNotificationEvents
import com.example.dayflow.ui.block_apps_notification.vm.BlockAppsNotificationInteractions
import com.example.dayflow.ui.block_apps_notification.vm.BlockAppsNotificationUiState
import com.example.dayflow.ui.block_apps_notification.vm.BlockAppsNotificationViewModel
import com.example.dayflow.ui.composable.ErrorContent
import com.example.dayflow.ui.composable.LoadingContent
import com.example.dayflow.ui.composable.PrimaryAppBar
import com.example.dayflow.ui.composable.VisibleContent
import com.example.dayflow.ui.theme.spacing
import com.example.dayflow.ui.utils.ContentStatus
import com.example.dayflow.ui.utils.EventHandler

@Composable
fun BlockAppsNotificationScreen(
    navController: NavController,
    viewModel: BlockAppsNotificationViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    EventHandler(effects = viewModel.events) { event, _ ->
        when (event) {
            BlockAppsNotificationEvents.NavigateToBack -> navController.popBackStack()
        }
    }
    BlockAppsNotificationContent(state = state, interactions = viewModel)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun BlockAppsNotificationContent(
    state: BlockAppsNotificationUiState,
    interactions: BlockAppsNotificationInteractions
) {
    LoadingContent(isVisible = state.contentStatus == ContentStatus.LOADING)
    VisibleContent(isVisible = state.contentStatus == ContentStatus.VISIBLE) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(MaterialTheme.spacing.space16),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.space16)
        ) {
            item {
                PrimaryAppBar(
                    title = stringResource(R.string.apps_notification),
                    onClickBack = interactions::onClickBack
                )
            }

            stickyHeader {
                Text(
                    text = stringResource(R.string.block_app_notification_until_session),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.outline
                )
            }

            items(state.appsInfo) { app ->
                BlockAppInfoItem(
                    state = app,
                    onChangeState = {
                        if (it) interactions.onBlockApp(app)
                        else interactions.onRemoveBlockedApp(app)
                    }
                )
            }
        }
    }
    ErrorContent(
        isVisible = state.contentStatus == ContentStatus.FAILURE,
        onTryAgain = interactions::initData
    )
}
