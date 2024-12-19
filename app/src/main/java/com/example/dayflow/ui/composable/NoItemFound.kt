package com.example.dayflow.ui.composable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.dayflow.R
import com.example.dayflow.ui.theme.spacing

@Composable
fun NoItemFound(
    modifier: Modifier = Modifier,
    isVisible: Boolean,
    isButtonVisible: Boolean = true,
    buttonText: String = stringResource(R.string.add),
    enterTransition: EnterTransition = fadeIn(tween(500)),
    exitTransition: ExitTransition = fadeOut(tween(500)),
    onClickAdd: () -> Unit = {},
) {
    AnimatedVisibility(
        modifier = modifier.fillMaxSize(),
        visible = isVisible,
        enter = enterTransition,
        exit = exitTransition,
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val rawComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.lottie_no_item_found))
            LottieAnimation(
                modifier = Modifier.height((LocalConfiguration.current.screenHeightDp / 3).dp),
                composition = rawComposition
            )
            if (isButtonVisible)
                PrimaryTextButton(
                    modifier = Modifier.padding(MaterialTheme.spacing.space16),
                    text = buttonText,
                    onClick = onClickAdd
                )
        }
    }
}