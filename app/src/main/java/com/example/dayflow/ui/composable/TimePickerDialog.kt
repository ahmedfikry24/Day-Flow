package com.example.dayflow.ui.composable

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.dayflow.R

@Composable
fun TimePickerDialog(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    content: @Composable () -> Unit,
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        dismissButton = {
            PrimaryTextButton(
                text = stringResource(R.string.cancel),
                containerColor = MaterialTheme.colorScheme.outlineVariant,
                contentColor = MaterialTheme.colorScheme.outline,
                onClick = onDismiss
            )
        },
        confirmButton = {
            PrimaryTextButton(
                text = stringResource(R.string.ok),
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                onClick = onConfirm
            )
        },
        text = { content() }
    )
}