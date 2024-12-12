package com.example.dayflow.ui.daily_tasks.composable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.dayflow.R
import com.example.dayflow.ui.composable.PrimaryTextButton
import com.example.dayflow.ui.theme.spacing

@Composable
fun DailyTaskTabs(
    modifier: Modifier = Modifier,
    isTabsVisible: Boolean,
    isDoneVisible: Boolean,
    onClickDone: () -> Unit,
    onClickInProgress: () -> Unit,
) {
    AnimatedVisibility(
        modifier = modifier,
        visible = isTabsVisible,
        enter = fadeIn(tween(200)) + expandVertically(tween(200)),
        exit = fadeOut(tween(200)) + shrinkVertically(tween(200))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.space16)
        ) {
            PrimaryTextButton(
                modifier = Modifier.weight(1f),
                text = stringResource(R.string.done),
                containerColor = if (isDoneVisible) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.outlineVariant,
                contentColor = if (isDoneVisible) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.outline,
                border = if (isDoneVisible) BorderStroke(
                    1.dp,
                    MaterialTheme.colorScheme.outline
                ) else null,
                shape = CircleShape,
                onClick = onClickDone
            )
            PrimaryTextButton(
                modifier = Modifier.weight(1f),
                text = "In Progress",
                containerColor = if (!isDoneVisible) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.outlineVariant,
                contentColor = if (!isDoneVisible) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.outline,
                border = if (!isDoneVisible) BorderStroke(
                    1.dp,
                    MaterialTheme.colorScheme.outline
                ) else null,
                shape = CircleShape,
                onClick = onClickInProgress
            )

        }
    }
}
