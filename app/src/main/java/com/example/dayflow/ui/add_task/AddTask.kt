package com.example.dayflow.ui.add_task

import android.provider.Settings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.dayflow.R
import com.example.dayflow.ui.composable.DatePickerModal
import com.example.dayflow.ui.composable.PrimaryDialog
import com.example.dayflow.ui.composable.PrimaryTextButton
import com.example.dayflow.ui.composable.PrimaryTextField
import com.example.dayflow.ui.composable.TimePickerModal
import com.example.dayflow.ui.theme.spacing
import com.example.dayflow.ui.utils.UiTestTags
import com.example.dayflow.ui.utils.formatTimeDigits
import com.example.dayflow.ui.utils.interaction.AddTaskInteraction
import com.example.dayflow.ui.utils.isScheduleAlarmPermissionGranted
import com.example.dayflow.ui.utils.ui_state.AddTaskUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTask(
    modifier: Modifier = Modifier,
    state: AddTaskUiState,
    interaction: AddTaskInteraction,
    isScheduleRequire: Boolean = true,
    onCancel: () -> Unit,
) {
    val context = LocalContext.current
    var isDateVisible by remember { mutableStateOf(false) }
    var isTimeVisible by remember { mutableStateOf(false) }
    var isAlarmPermissionVisible by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(MaterialTheme.spacing.space16),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.space16)
        ) {
            PrimaryTextButton(
                modifier = Modifier.align(Alignment.End),
                text = stringResource(R.string.cancel),
                containerColor = MaterialTheme.colorScheme.outlineVariant,
                contentColor = MaterialTheme.colorScheme.outline,
                onClick = onCancel
            )

            PrimaryTextField(
                value = state.title,
                isError = state.titleError,
                hint = stringResource(R.string.headline),
                imeAction = ImeAction.Next,
                onChangeValue = interaction::onTitleChange
            )
            val descriptionHeight = (LocalConfiguration.current.screenHeightDp / 3).dp
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(descriptionHeight),
                value = state.description,
                placeholder = {
                    Text(
                        text = stringResource(R.string.description),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                singleLine = false,
                isError = false,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.secondary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    errorBorderColor = MaterialTheme.colorScheme.error,
                ),
                textStyle = MaterialTheme.typography.titleMedium,
                shape = RoundedCornerShape(MaterialTheme.spacing.space4),
                onValueChange = interaction::onDescriptionChange
            )
            if (isScheduleRequire)
                ScheduleTaskDate(
                    date = state.date,
                    time = state.time,
                    onClickDate = {
                        if (context.isScheduleAlarmPermissionGranted() &&
                            Settings.canDrawOverlays(context)
                        )
                            isDateVisible = true
                        else isAlarmPermissionVisible = true
                    },
                    onClickTime = {
                        if (context.isScheduleAlarmPermissionGranted() &&
                            Settings.canDrawOverlays(context)
                        )
                            isTimeVisible = true
                        else isAlarmPermissionVisible = true
                    }
                )
        }
        PrimaryTextButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MaterialTheme.spacing.space16)
                .padding(bottom = MaterialTheme.spacing.space16),
            text = stringResource(R.string.add_task),
            onClick = interaction::addTask
        )
    }

    if (isDateVisible)
        DatePickerModal(
            modifier = Modifier.testTag(UiTestTags.DATE_PICKER_MODAL),
            onDismiss = { isDateVisible = false },
            onDateSelected = interaction::onDateChange
        )
    if (isTimeVisible)
        TimePickerModal(
            modifier = Modifier.testTag(UiTestTags.TIME_PICKER_MODAL),
            onConfirm = {
                val time = "${it.hour}:${it.minute}"
                interaction.onTimeChange(time.formatTimeDigits())
                isTimeVisible = false
            },
            onDismiss = { isTimeVisible = false }
        )

    if (state.isSchedulingEmptyDialogVisibility)
        PrimaryDialog(
            title = stringResource(R.string.not_complete),
            text = stringResource(R.string.if_scheduling_task_you_must_select_date_and_time),
            onConfirm = interaction::controlEmptySchedulingDialogVisibility,
            onDismiss = interaction::controlEmptySchedulingDialogVisibility
        )
    if (state.isScheduledUnValid)
        PrimaryDialog(
            title = stringResource(R.string.unvalid),
            text = stringResource(R.string.you_select_a_date_or_time_already_gone_please_choose_correct_date),
            onConfirm = interaction::controlUnValidScheduledDialogVisibility,
            onDismiss = interaction::controlUnValidScheduledDialogVisibility,
        )

    if (isAlarmPermissionVisible)
        ScheduleAlarmPermissionsDialog(
            onPermissionsGranted = {
                isAlarmPermissionVisible = false
                isDateVisible = true
            },
            onDismiss = { isAlarmPermissionVisible = false }
        )
}

