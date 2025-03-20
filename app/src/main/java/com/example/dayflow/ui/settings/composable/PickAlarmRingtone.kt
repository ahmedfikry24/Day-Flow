package com.example.dayflow.ui.settings.composable

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.dayflow.R
import com.example.dayflow.ui.composable.PrimaryDialog
import com.example.dayflow.ui.theme.spacing
import com.example.dayflow.ui.utils.goToSettings
import com.example.dayflow.ui.utils.parcelable

@Composable
fun PickAlarmRingtone(
    modifier: Modifier = Modifier,
    onSaveRingtone: (Uri) -> Unit
) {
    var isOpen by remember { mutableStateOf(false) }
    val transition = updateTransition(isOpen, label = "transition")
    val backgroundColor by transition.animateColor {
        if (it) MaterialTheme.colorScheme.surfaceDim
        else MaterialTheme.colorScheme.background
    }
    val arrowPickDirection by transition.animateFloat { if (it) 90f else 0f }

    val ringtonePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val ringtone = result.data?.parcelable<Uri>(RingtoneManager.EXTRA_RINGTONE_PICKED_URI)
            if (ringtone != null)
                onSaveRingtone(ringtone)
        }
    }
    val context = LocalContext.current
    val musicPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val uri = result.data?.data
        if (uri != null) {
            context.contentResolver.takePersistableUriPermission(
                uri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            onSaveRingtone(uri)
        }

    }
    var openMediaRationalDialog by remember { mutableStateOf(false) }
    var openMediaGoSettingsDialog by remember { mutableStateOf(false) }

    val activity = context as Activity
    val mediaPermission = if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2)
        Manifest.permission.READ_EXTERNAL_STORAGE else Manifest.permission.READ_MEDIA_AUDIO
    val mediaPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            when {
                granted -> launchMediaPicker(musicPicker)
                ActivityCompat.shouldShowRequestPermissionRationale(
                    activity,
                    mediaPermission
                ) -> openMediaRationalDialog = true

                else -> openMediaGoSettingsDialog = true
            }
        }
    )

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .heightIn(min = 48.dp)
            .padding(horizontal = MaterialTheme.spacing.space8),
        onClick = { isOpen = !isOpen },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(backgroundColor),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.space8)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.change_alarm_ringtone),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Icon(
                    modifier = Modifier.rotate(arrowPickDirection),
                    imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            transition.AnimatedVisibility(visible = { it }) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.space8)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(32.dp)
                            .clickable {
                                isOpen = false
                                launchRingtonePicker(ringtonePicker)
                            },
                        contentAlignment = Alignment.CenterStart

                    ) {
                        Text(
                            text = stringResource(R.string.from_defaults),
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(32.dp)
                            .clickable {
                                isOpen = false
                                if (checkMediaPermission(context, mediaPermission)) {
                                    launchMediaPicker(musicPicker)
                                } else mediaPermissionLauncher.launch(mediaPermission)
                            },
                        contentAlignment = Alignment.CenterStart

                    ) {
                        Text(
                            text = stringResource(R.string.from_device),
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }

    if (openMediaRationalDialog)
        PrimaryDialog(
            title = stringResource(R.string.media_permission),
            text = stringResource(R.string.to_change_alarm_ringtone_you_must_give_us_media_permission_to_help_you_pick_audio_file),
            onConfirm = {
                openMediaRationalDialog = false
                mediaPermissionLauncher.launch(mediaPermission)
            },
            onDismiss = { openMediaRationalDialog = false }
        )
    if (openMediaGoSettingsDialog)
        PrimaryDialog(
            title = stringResource(R.string.media_permission_needed),
            text = stringResource(R.string.the_app_needs_media_permission_to_change_your_alarm_ringtone_please_grant_it_in_the_app_settings),
            onConfirm = {
                openMediaGoSettingsDialog = false
                activity.goToSettings()
            },
            onDismiss = { openMediaGoSettingsDialog = false }
        )
}

private fun launchRingtonePicker(picker: ManagedActivityResultLauncher<Intent, ActivityResult>) {
    val intent = Intent(RingtoneManager.ACTION_RINGTONE_PICKER).apply {
        putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM)
        putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Alarm Tone")
        putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false)
        putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true)
    }
    picker.launch(intent)
}

private fun launchMediaPicker(launcher: ManagedActivityResultLauncher<Intent, ActivityResult>) {
    val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
        addCategory(Intent.CATEGORY_OPENABLE)
        type = "audio/*"
    }
    launcher.launch(intent)
}

private fun checkMediaPermission(context: Context, permission: String): Boolean {
    return ContextCompat.checkSelfPermission(
        context,
        permission
    ) == PackageManager.PERMISSION_GRANTED
}
