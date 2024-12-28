package com.example.dayflow.ui.work_session.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.example.dayflow.R
import com.example.dayflow.ui.theme.spacing
import com.example.dayflow.ui.utils.formatSessionTime
import com.example.dayflow.ui.work_session.vm.WorkSessionInteractions
import com.example.dayflow.ui.work_session.vm.WorkSessionUiState

@Composable
fun SessionCountDown(
    modifier: Modifier = Modifier,
    state: WorkSessionUiState,
    interactions: WorkSessionInteractions
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val progressSize = (LocalConfiguration.current.screenWidthDp / 3).dp
        Box(contentAlignment = Alignment.Center) {
            CircularProgressIndicator(
                progress = { state.sessionRemainingTime.toFloat() / state.sessionDuration.toFloat() },
                modifier = Modifier.size(progressSize),
                color = MaterialTheme.colorScheme.tertiary,
                strokeWidth = 8.dp,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
            )

            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_plant),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onTertiaryContainer
            )
        }

        Text(
            modifier = Modifier.padding(vertical = MaterialTheme.spacing.space16),
            text = state.sessionRemainingTime.formatSessionTime(),
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onSurface
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.space16)
        ) {
            FilledIconButton(
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                shape = CircleShape,
                onClick = {if (state.isRunning) interactions.pauseSession() else interactions.resumeSession()}
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(if (state.isRunning) R.drawable.ic_stop_watch_pause else R.drawable.ic_stop_watch_play),
                    contentDescription = null,
                )
            }

            FilledIconButton(
                enabled = !state.isRunning,
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                shape = CircleShape,
                onClick = interactions::finishSession
            ) {
                Icon(
                    imageVector = Icons.Outlined.Refresh,
                    contentDescription = null,
                )
            }
        }
    }
}
