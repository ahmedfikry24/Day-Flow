package com.example.dayflow.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.dayflow.R
import com.example.dayflow.navigation.AppDestination
import com.example.dayflow.ui.composable.ErrorContent
import com.example.dayflow.ui.composable.LoadingContent
import com.example.dayflow.ui.composable.VisibleContent
import com.example.dayflow.ui.settings.composable.SettingMenuItem
import com.example.dayflow.ui.settings.vm.SettingsEvents
import com.example.dayflow.ui.settings.vm.SettingsInteractions
import com.example.dayflow.ui.settings.vm.SettingsUiState
import com.example.dayflow.ui.settings.vm.SettingsViewModel
import com.example.dayflow.ui.theme.spacing
import com.example.dayflow.ui.utils.ContentStatus
import com.example.dayflow.ui.utils.EventHandler

@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    EventHandler(effects = viewModel.events) { event, _ ->
        when (event) {
            SettingsEvents.NavigateToBlockApps -> navController.navigate(AppDestination.BlockAppsNotification)
        }
    }
    SettingsContent(state = state, interactions = viewModel)
}

@Composable
private fun SettingsContent(
    state: SettingsUiState,
    interactions: SettingsInteractions
) {
    LoadingContent(isVisible = state.contentStatus == ContentStatus.LOADING)
    VisibleContent(isVisible = state.contentStatus == ContentStatus.VISIBLE) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(MaterialTheme.spacing.space16),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.space16)
        ) {
            SettingMenuItem(
                title = stringResource(R.string.block_apps_notifications),
                onClick = interactions::onClickBlockApps
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            SettingMenuItem(
                title = stringResource(R.string.dark_theme),
                isClickEnable = false,
                onClick = {}
            ) {
                Switch(
                    checked = !state.isLightTheme,
                    onCheckedChange = { interactions.onToggleTheme() }
                )
            }
        }
    }
    ErrorContent(
        isVisible = state.contentStatus == ContentStatus.FAILURE,
        onTryAgain = interactions::initData
    )
}
