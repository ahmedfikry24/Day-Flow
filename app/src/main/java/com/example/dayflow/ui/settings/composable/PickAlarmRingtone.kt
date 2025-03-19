package com.example.dayflow.ui.settings.composable

import android.app.Activity
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.dayflow.R
import com.example.dayflow.ui.theme.spacing
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

    val musicPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val uri = result.data?.data
        if (uri != null)
            onSaveRingtone(uri)
    }
    Surface(
        modifier = modifier
            .fillMaxWidth()
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
                                launchMediaPicker(musicPicker)
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
}


@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun PreviewPick() {
    PickAlarmRingtone() {}
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

