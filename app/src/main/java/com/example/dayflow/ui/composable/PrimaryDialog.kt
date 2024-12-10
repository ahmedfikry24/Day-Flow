package com.example.dayflow.ui.composable

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.example.dayflow.R
import com.example.dayflow.ui.theme.spacing

@Composable
fun PrimaryDialog(
    modifier: Modifier = Modifier,
    title: String,
    text: String,
    confirmText: String = stringResource(R.string.ok),
    cancelText: String = stringResource(R.string.cancel),
    confirmContainerColor: Color = MaterialTheme.colorScheme.secondary,
    confirmContentColor: Color = MaterialTheme.colorScheme.onSecondary,
    cancelContainerColor: Color = MaterialTheme.colorScheme.outlineVariant,
    cancelContentColor: Color = MaterialTheme.colorScheme.outline,
    onConfirm: () -> Unit,
    onCancel: (() -> Unit)? = null,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.onPrimary,
        shape = RoundedCornerShape(MaterialTheme.spacing.space8),
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
        },
        text = {
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium,
                color = Color.Black
            )
        },
        confirmButton = {
            PrimaryTextButton(
                text = confirmText,
                containerColor = confirmContainerColor,
                contentColor = confirmContentColor,
                onClick = onConfirm
            )
        },
        dismissButton = {
            if (onCancel != null)
                PrimaryTextButton(
                    text = cancelText,
                    containerColor = cancelContainerColor,
                    contentColor = cancelContentColor,
                    onClick = onCancel
                )
        }
    )
}