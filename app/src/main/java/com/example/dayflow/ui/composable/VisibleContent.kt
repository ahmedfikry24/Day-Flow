package com.example.dayflow.ui.composable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


@Composable
fun VisibleContent(
    isVisible: Boolean,
    enterTransition: EnterTransition = fadeIn(tween(500)) + scaleIn(tween(500)),
    exitTransition: ExitTransition = fadeOut(tween(500)) + scaleOut(tween(500)),
    content: @Composable AnimatedVisibilityScope.() -> Unit,
) {
    AnimatedVisibility(
        modifier = Modifier.fillMaxSize(),
        visible = isVisible,
        enter = enterTransition,
        exit = exitTransition,
        content = content
    )
}
