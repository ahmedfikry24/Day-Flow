package com.example.dayflow.ui.composable

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.dayflow.ui.theme.green

object DefaultSwipeItemToDismiss {

    @Composable
    fun colors(): SwipeItemColors {
        return SwipeItemColors(
            containerRightColor = MaterialTheme.colorScheme.error,
            containerLeftColor = green,
        )
    }
}

data class SwipeItemColors(
    val containerRightColor: Color,
    val containerLeftColor: Color,
)