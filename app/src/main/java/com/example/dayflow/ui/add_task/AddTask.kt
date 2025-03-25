package com.example.dayflow.ui.add_task

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.PowerManager
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.core.net.toUri
import com.example.dayflow.R
import com.example.dayflow.ui.composable.DatePickerModal
import com.example.dayflow.ui.composable.PrimaryDialog
import com.example.dayflow.ui.composable.PrimaryTextButton
import com.example.dayflow.ui.composable.PrimaryTextField
import com.example.dayflow.ui.composable.TimePickerModal
import com.example.dayflow.ui.theme.spacing
import com.example.dayflow.ui.utils.UiTestTags
import com.example.dayflow.ui.utils.checkScheduleAlarmPermission
import com.example.dayflow.ui.utils.formatTimeDigits
import com.example.dayflow.ui.utils.interaction.AddTaskInteraction
import com.example.dayflow.ui.utils.requestScheduleAlarmPermission
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
    val overlayPermission = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (Settings.canDrawOverlays(context))
            isDateVisible = true
    }

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
                        checkRequireAlarmPermissions(
                            context = context,
                            requestSchedulePermission = interaction::controlScheduleAlarmDialogVisibility,
                            onPermissionsGranted = {
                                if (!Settings.canDrawOverlays(context)) {
                                    val intent = Intent(
                                        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                        "package:${context.packageName}".toUri()
                                    )
                                    overlayPermission.launch(intent)
                                } else isDateVisible = true
                            }
                        )
                    },
                    onClickTime = {
                        if (context.checkScheduleAlarmPermission())
                            isTimeVisible = true
                        else interaction.controlScheduleAlarmDialogVisibility()
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

    if (state.canScheduleAlarmDialogVisibility)
        PrimaryDialog(
            title = stringResource(R.string.permission),
            text = stringResource(R.string.app_you_must_give_app_a_permission_to_schedule_your_tasks_to_give_you_full_app_functionality_go_to_settings_and_give_us_the_permission),
            onConfirm = {
                interaction.controlScheduleAlarmDialogVisibility()
                context.requestScheduleAlarmPermission()
            },
            onCancel = interaction::controlScheduleAlarmDialogVisibility,
            onDismiss = interaction::controlScheduleAlarmDialogVisibility
        )

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
}

private fun checkRequireAlarmPermissions(
    context: Context,
    requestSchedulePermission: () -> Unit,
    onPermissionsGranted: () -> Unit
) {
    if (context.checkScheduleAlarmPermission()) {
        if (isBatteryOptimizationEnabled(context)) {
            AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.battery_optimization))
                .setMessage(context.getString(R.string.to_make_app_works_correctly_please_disable_battery_optimization_for_this_app))
                .setPositiveButton(context.getString(R.string.ok)) { _, _ ->
                    requestIgnoreBatteryOptimizations(context)
                }
                .show()
        } else onPermissionsGranted()
    } else requestSchedulePermission()
}

fun isBatteryOptimizationEnabled(context: Context): Boolean {
    val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
    val packageName = context.packageName
    return !powerManager.isIgnoringBatteryOptimizations(packageName)
}

@SuppressLint("BatteryLife")
private fun requestIgnoreBatteryOptimizations(context: Context) {
    val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
    intent.data = "package:${context.packageName}".toUri()
    context.startActivity(intent)
}