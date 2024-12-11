package com.example.dayflow.ui.composable

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
import com.example.dayflow.ui.utils.goToSettings
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun rememberScheduleExactAlarm(): PermissionState? {

    val activity = LocalContext.current as MainActivity
    var isRationaleVisible by remember { mutableStateOf(false) }
    var isGoSettingsVisible by remember { mutableStateOf(false) }

    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val alarmPermission =
            rememberPermissionState(android.Manifest.permission.SCHEDULE_EXACT_ALARM) {
                isRationaleVisible = !it
                isGoSettingsVisible = !it
            }
        LaunchedEffect(Unit) {
            alarmPermission.launchPermissionRequest()
        }
        when {
            alarmPermission.status.isGranted -> Unit
            alarmPermission.status.shouldShowRationale -> {
                if (isRationaleVisible)
                    PrimaryDialog(
                        title = stringResource(R.string.permission),
                        text = stringResource(R.string.you_must_give_app_a_permission_to_schedule_your_tasks),
                        onConfirm = { alarmPermission.launchPermissionRequest() },
                        onDismiss = { isRationaleVisible = false },
                        onCancel = { isRationaleVisible = false }
                    )
            }

            else -> {
                if (isGoSettingsVisible)
                    PrimaryDialog(
                        title = stringResource(R.string.permission),
                        text = stringResource(R.string.app_you_must_give_app_a_permission_to_schedule_your_tasks_to_give_you_full_app_functionality_go_to_settings_and_give_us_the_permission),
                        onConfirm = { activity.goToSettings() },
                        onDismiss = { isRationaleVisible = false },
                        onCancel = { isRationaleVisible = false }
                    )
            }
        }
        alarmPermission
    } else null
}


