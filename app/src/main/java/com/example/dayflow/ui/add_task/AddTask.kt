package com.example.dayflow.ui.add_task

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.dayflow.R
import com.example.dayflow.ui.composable.DatePickerModal
import com.example.dayflow.ui.composable.PrimaryTextButton
import com.example.dayflow.ui.composable.PrimaryTextField
import com.example.dayflow.ui.theme.spacing
import com.example.dayflow.ui.utils.interaction.AddTaskInteraction
import com.example.dayflow.ui.utils.ui_state.AddTaskUiState

@Composable
fun AddTask(
    modifier: Modifier = Modifier,
    state: AddTaskUiState,
    interaction: AddTaskInteraction,
    onCancel: () -> Unit,
) {
    var isDateVisible by remember { mutableStateOf(false) }
    Column(modifier = modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(MaterialTheme.spacing.space16),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.space16)
        ) {
            PrimaryTextButton(
                modifier = Modifier.align(Alignment.End),
                text = stringResource(R.string.cancel),
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
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
            PrimaryTextField(
                modifier = Modifier.height(descriptionHeight),
                value = state.description,
                isError = false,
                hint = stringResource(R.string.description),
                isSingleLine = false,
                onChangeValue = interaction::onDescriptionChange
            )
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.space8)
            ) {
                Text(
                    stringResource(R.string.schedule_task),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .border(1.dp, color = MaterialTheme.colorScheme.outlineVariant)
                        .clip(
                            RoundedCornerShape(MaterialTheme.spacing.space4)
                        )
                        .clickable { isDateVisible = true },
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        modifier = Modifier.padding(start = MaterialTheme.spacing.space8),
                        text = state.date,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        }
        PrimaryTextButton(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.add_task),
            onClick = interaction::addTask
        )
    }
    if (isDateVisible)
        DatePickerModal(
            onDismiss = { isDateVisible = false },
            onDateSelected = interaction::onDateChange
        )
}
