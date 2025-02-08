package com.example.dayflow.ui.work_session.composable

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.dayflow.R
import com.example.dayflow.ui.composable.PrimaryTextButton
import com.example.dayflow.ui.theme.spacing
import com.example.dayflow.ui.work_session.vm.WorkSessionInteractions
import com.example.dayflow.ui.work_session.vm.WorkSessionUiState

@Composable
fun SessionInfo(
    modifier: Modifier = Modifier,
    state: WorkSessionUiState,
    interactions: WorkSessionInteractions
) {
    val itemMaxWidth = (LocalConfiguration.current.screenWidthDp * 2 / 3).dp
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(MaterialTheme.spacing.space16),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.get_ready_to_focus),
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            modifier = Modifier
                .width(itemMaxWidth)
                .padding(vertical = MaterialTheme.spacing.space16),
            text = stringResource(R.string.set_your_session_time_and_start_then_in_the_end_of_session_will_notify_you),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Surface(
            modifier = Modifier
                .wrapContentSize()
                .padding(bottom = MaterialTheme.spacing.space24),
            shape = RoundedCornerShape(MaterialTheme.spacing.space4),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(
                    modifier = Modifier.padding(MaterialTheme.spacing.space16),
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.space8),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = state.sessionDurationMin.toString(),
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = stringResource(R.string.minutes),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Column {
                    IconButton(
                        enabled = state.sessionDurationMin <= 115,
                        colors = IconButtonDefaults.iconButtonColors(contentColor = MaterialTheme.colorScheme.onSurface),
                        onClick = interactions::plusSessionDurationMin
                    ) {
                        Icon(
                            modifier = Modifier.rotate(90f),
                            imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowLeft,
                            contentDescription = stringResource(R.string.plus_session_duration_icon)
                        )
                    }
                    IconButton(
                        enabled = state.sessionDurationMin >= 15,
                        colors = IconButtonDefaults.iconButtonColors(contentColor = MaterialTheme.colorScheme.onSurface),
                        onClick = interactions::minusSessionDurationMin
                    ) {
                        Icon(
                            modifier = Modifier.rotate(270f),
                            imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowLeft,
                            contentDescription = stringResource(R.string.minus_session_duration_icon)
                        )
                    }
                }
            }
        }

        PrimaryTextButton(
            modifier = Modifier.width(itemMaxWidth),
            text = stringResource(R.string.start_session),
            onClick = interactions::startSession
        )
    }
}
