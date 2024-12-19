package com.example.dayflow.ui.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.example.dayflow.ui.theme.spacing

@Composable
fun SwipeTaskBackground(
    modifier: Modifier = Modifier,
    iconsRes: List<Int>,
    iconsArrangement: Arrangement.Horizontal = Arrangement.SpaceBetween
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(MaterialTheme.spacing.space8),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = iconsArrangement,
            verticalAlignment = Alignment.CenterVertically
        ) {
            iconsRes.forEach { res ->
                Icon(
                    imageVector = ImageVector.vectorResource(res),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onError
                )
            }
        }
    }
}