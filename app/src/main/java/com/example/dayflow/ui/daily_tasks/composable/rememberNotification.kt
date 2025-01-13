package com.example.dayflow.ui.daily_tasks.composable

import android.Manifest
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.example.dayflow.MainActivity
import com.example.dayflow.R
import com.example.dayflow.ui.composable.PrimaryDialog
import com.example.dayflow.ui.utils.goToSettings
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RememberNotificationPermission() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

        val activity = LocalContext.current as MainActivity
        var isRationaleVisible by remember { mutableStateOf(false) }
        var isGoSettingsVisible by remember { mutableStateOf(false) }


        val notificationPermission = rememberPermissionState(
            permission = Manifest.permission.POST_NOTIFICATIONS
        ) {
            isRationaleVisible = !it
            isGoSettingsVisible = !it
        }
        LaunchedEffect(Unit) {
            if (!notificationPermission.status.isGranted)
                notificationPermission.launchPermissionRequest()
        }

        when {
            notificationPermission.status.isGranted -> Unit
            notificationPermission.status.shouldShowRationale -> {
                if (isRationaleVisible)
                    PrimaryDialog(
                        title = stringResource(R.string.permission),
                        text = stringResource(R.string.this_app_require_to_receive_tasks_notification_please_grant_the_permission),
                        onConfirm = { notificationPermission.launchPermissionRequest() },
                        onCancel = { isRationaleVisible = false },
                        onDismiss = { isRationaleVisible = false }
                    )
            }

            else -> {
                if (isGoSettingsVisible)
                    PrimaryDialog(
                        title = stringResource(R.string.permission),
                        text = stringResource(R.string.this_app_require_to_receive_tasks_notification_please_allow_access_in_the_settings),
                        onConfirm = {
                            isGoSettingsVisible = false
                            activity.goToSettings()
                        },
                        onCancel = { isGoSettingsVisible = false },
                        onDismiss = { isGoSettingsVisible = false }
                    )
            }
        }
    }
}