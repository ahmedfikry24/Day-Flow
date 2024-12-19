package com.example.dayflow.ui.composable

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.dayflow.R
import com.example.dayflow.ui.theme.spacing
import com.example.dayflow.ui.utils.applyPadding
import com.example.dayflow.ui.utils.ui_state.INITIAL_DATE
import com.example.dayflow.ui.utils.ui_state.TaskUiState

@Composable
fun TaskItem(
    modifier: Modifier = Modifier,
    state: TaskUiState,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainer,
    onClick: (TaskUiState) -> Unit = {},
) {
    Box(modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Surface(
            modifier = modifier
                .fillMaxWidth()
                .applyPadding(
                    state.isDone,
                    PaddingValues(horizontal = MaterialTheme.spacing.space8)
                ),
            shape = RoundedCornerShape(MaterialTheme.spacing.space4),
            color = containerColor,
            border = BorderStroke(1.dp, color = MaterialTheme.colorScheme.outlineVariant),
            enabled = !state.isDone,
            onClick = { onClick(state) }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(MaterialTheme.spacing.space8),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.space8)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(
                        MaterialTheme.spacing.space8
                    )
                ) {
                    if (!state.isDone)
                        VerticalDivider(
                            modifier = Modifier.height(50.dp),
                            thickness = 2.dp,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.space8)
                    ) {
                        Text(
                            state.title,
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                        Text(
                            state.description,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
                if (state.date != INITIAL_DATE)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.space4)
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.ic_clock),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.outline
                            )
                            Text(
                                state.time,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.outline,
                            )
                        }
                        Spacer(modifier = Modifier.width(MaterialTheme.spacing.space8))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.space4)
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.ic_calendar),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.outline
                            )
                            Text(
                                state.date,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.outline,
                            )
                        }
                    }
            }
        }
        if (state.isDone)
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 2.dp,
                color = MaterialTheme.colorScheme.onBackground
            )
    }
}