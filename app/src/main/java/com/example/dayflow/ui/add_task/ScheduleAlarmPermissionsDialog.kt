package com.example.dayflow.ui.add_task

import android.content.Intent
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.provider.Settings
import android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import com.example.dayflow.R
import com.example.dayflow.ui.composable.PrimaryTextButton
import com.example.dayflow.ui.theme.spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleAlarmPermissionsDialog(
    modifier: Modifier = Modifier,
    onPermissionsGranted: () -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var permissionStatus by remember { mutableStateOf(getNeededPermission(context)) }


    val checkDrawOverAppsRequire = {
        permissionStatus =
            if (getNeededPermission(context) == AlarmPermissionStatus.SCHEDULE
                && !Settings.canDrawOverlays(context)
            )
                AlarmPermissionStatus.DRAW_OVER_APPS
            else AlarmPermissionStatus.NONE
    }

    val scheduleLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { checkDrawOverAppsRequire() }

    val overlayLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (Settings.canDrawOverlays(context))
            onPermissionsGranted()
    }

    val isSchedulePermission = permissionStatus == AlarmPermissionStatus.SCHEDULE
    val permissionType = if (isSchedulePermission)
        stringResource(R.string.schedule_alarm)
    else stringResource(R.string.draw_over_apps)
    val restOfPermissionDescription = if (isSchedulePermission)
        stringResource(R.string.to_ring_alarm_in_exact_time)
    else stringResource(R.string.to_make_alerting_works_correctly)
    val permissionText = buildAnnotatedString {
        withStyle(
            style = SpanStyle(
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.lato_regular, FontWeight.Normal)),
            )
        ) {
            append(stringResource(R.string.allow))
            append(" ")
        }
        withStyle(
            style = SpanStyle(
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.lato_bold, FontWeight.Bold)),
            )
        ) { append(permissionType) }

        withStyle(
            style = SpanStyle(
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.lato_regular, FontWeight.Normal)),
            )
        ) {
            append(" ")
            append(restOfPermissionDescription)
        }
    }

    BasicAlertDialog(
        modifier = modifier,
        onDismissRequest = onDismiss
    ) {
        Crossfade(targetState = permissionStatus) {
            when (it) {
                AlarmPermissionStatus.SCHEDULE -> PermissionBody(
                    iconId = R.drawable.ic_clock,
                    text = permissionText,
                    onClickDeny = { checkDrawOverAppsRequire() },
                    onClickAllow = {
                        if (VERSION.SDK_INT >= VERSION_CODES.S)
                            scheduleLauncher.launch(
                                Intent(ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                            )
                    }
                )

                AlarmPermissionStatus.DRAW_OVER_APPS -> PermissionBody(
                    iconId = R.drawable.ic_layers,
                    text = permissionText,
                    onClickDeny = onDismiss,
                    onClickAllow = {
                        overlayLauncher.launch(
                            Intent(
                                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                "package:${context.packageName}".toUri()
                            )
                        )
                    }
                )

                AlarmPermissionStatus.NONE -> onDismiss()
            }
        }
    }
}

@Composable
private fun PermissionBody(
    modifier: Modifier = Modifier,
    iconId: Int,
    text: AnnotatedString,
    onClickDeny: () -> Unit,
    onClickAllow: () -> Unit
) {
    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface)
            .padding(MaterialTheme.spacing.space16),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.space16)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.space8)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(iconId),
                contentDescription = stringResource(R.string.permissionicon),
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                text = text,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Row(
            modifier = Modifier.align(Alignment.End),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.space16)
        ) {
            PrimaryTextButton(
                text = stringResource(R.string.deny),
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface,
                onClick = onClickDeny
            )
            PrimaryTextButton(
                text = stringResource(R.string.allow),
                onClick = onClickAllow
            )
        }
    }
}