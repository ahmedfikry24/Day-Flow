package com.example.dayflow.ui.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.example.dayflow.R
import com.example.dayflow.ui.theme.green
import com.example.dayflow.ui.theme.spacing

@Composable
fun SwipeTaskBackground(
    modifier: Modifier = Modifier,
    state: SwipeToDismissBoxValue,
) {
    val containerColor = when (state) {
        SwipeToDismissBoxValue.StartToEnd -> green
        SwipeToDismissBoxValue.EndToStart -> MaterialTheme.colorScheme.error
        SwipeToDismissBoxValue.Settled -> Color.Transparent
    }
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(containerColor)
            .padding(MaterialTheme.spacing.space8),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_check_right),
                contentDescription = "done",
                tint = MaterialTheme.colorScheme.onError
            )
            Spacer(modifier = Modifier)
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_trash),
                contentDescription = "delete",
                tint = MaterialTheme.colorScheme.onError
            )
        }
    }
}