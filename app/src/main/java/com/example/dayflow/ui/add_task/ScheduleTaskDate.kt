package com.example.dayflow.ui.add_task

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.dayflow.R
import com.example.dayflow.ui.theme.spacing

@Composable
fun ScheduleTaskDate(
    modifier: Modifier = Modifier,
    date: String,
    time: String,
    onClickDate: () -> Unit,
    onClickTime: () -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.space16)
    ) {
        DateField(
            modifier = Modifier.weight(1f),
            value = date,
            onClick = onClickDate
        )
        DateField(
            modifier = Modifier.weight(1f),
            value = time,
            onClick = onClickTime
        )
    }
}


@Composable
private fun DateField(
    modifier: Modifier = Modifier,
    value: String,
    onClick: () -> Unit,
) {
    Column(
        modifier = modifier,
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
                .clip(RoundedCornerShape(MaterialTheme.spacing.space4))
                .clickable { onClick() },
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                modifier = Modifier.padding(start = MaterialTheme.spacing.space8),
                text = value,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}
