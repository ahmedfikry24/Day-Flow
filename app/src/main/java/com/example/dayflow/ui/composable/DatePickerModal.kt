package com.example.dayflow.ui.composable

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.dayflow.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    modifier: Modifier = Modifier,
    onDateSelected: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        confirmButton = {
            PrimaryTextButton(
                text = stringResource(R.string.ok),
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary
            ) {
                datePickerState.selectedDateMillis?.let {
                    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    onDateSelected(formatter.format(Date(it)))
                }
                onDismiss()
            }
        },
        dismissButton = {
            PrimaryTextButton(
                text = stringResource(R.string.cancel),
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary
            ) { onDismiss() }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}
