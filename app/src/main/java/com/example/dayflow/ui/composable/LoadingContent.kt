package com.example.dayflow.ui.composable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun LoadingContent(
    isVisible: Boolean,
    enterTransition: EnterTransition = fadeIn(tween(500)) + scaleIn(tween(500)),
    exitTransition: ExitTransition = fadeOut(tween(500)) + scaleOut(tween(500)),
) {
    val size = (LocalConfiguration.current.screenWidthDp * 0.2).dp
    AnimatedVisibility(
        modifier = Modifier.fillMaxSize(),
        visible = isVisible,
        enter = enterTransition,
        exit = exitTransition,
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(size),
                color = MaterialTheme.colorScheme.secondary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
                strokeWidth = 2.dp
            )
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun LoadingContentPreview() {
    LoadingContent(isVisible = true)
}
