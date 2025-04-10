package com.example.dayflow.ui.block_apps_notification.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import com.example.dayflow.ui.block_apps_notification.vm.BlockAppsNotificationUiState
import com.example.dayflow.ui.theme.spacing

@Composable
fun BlockAppInfoItem(
    modifier: Modifier = Modifier,
    state: BlockAppsNotificationUiState.BlockAppInfoUiState,
    isMultiSelectionOff: Boolean,
    onChangeState: (Boolean) -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.space16)
        ) {
            state.icon?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = null
                )
            }

            Text(
                text = state.name,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        if (isMultiSelectionOff)
            Switch(
                checked = state.isBlock,
                onCheckedChange = onChangeState
            )
    }
}
