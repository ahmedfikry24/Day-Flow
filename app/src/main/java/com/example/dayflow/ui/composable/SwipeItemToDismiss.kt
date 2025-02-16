package com.example.dayflow.ui.composable

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.dayflow.ui.utils.UiTestTags
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SwipeItemToDismiss(
    modifier: Modifier = Modifier,
    enableGesture: Boolean = true,
    enableSwipeStartToEnd: Boolean = true,
    enableSwipeEndToStart: Boolean = true,
    colors: SwipeItemColors = DefaultSwipeItemToDismiss.colors(),
    positionalThreshold: Float = (LocalConfiguration.current.screenWidthDp * 2 / 3).toFloat(),
    onSwipeRight: () -> Unit = {},
    onSwipeLeft: () -> Unit = {},
    backgroundContent: @Composable RowScope.() -> Unit,
    content: @Composable RowScope.() -> Unit,
) {
    var offsetX by remember { mutableFloatStateOf(0f) }
    val animatedOffsetX by animateFloatAsState(targetValue = offsetX, label = "offsetTransition")
    val scope = rememberCoroutineScope()
    val exitScreenWidth = (LocalConfiguration.current.screenWidthDp * 2).toFloat()

    Box(modifier = modifier.wrapContentSize()) {

        if (offsetX < 0)
            Row(
                modifier = Modifier
                    .matchParentSize()
                    .align(Alignment.CenterEnd)
                    .background(colors.containerRightColor),
                content = backgroundContent
            )

        if (offsetX > 0)
            Row(
                modifier = Modifier
                    .matchParentSize()
                    .align(Alignment.CenterStart)
                    .background(colors.containerLeftColor),
                content = backgroundContent
            )

        Row(
            modifier = Modifier
                .offset(x = animatedOffsetX.dp, y = 0.dp)
                .pointerInput(Unit) {
                    if (enableGesture)
                        detectHorizontalDragGestures(
                            onHorizontalDrag = { change, dragAmount ->
                                change.consume()
                                if ((dragAmount > 0 && enableSwipeStartToEnd) || (dragAmount < 0 && enableSwipeEndToStart))
                                    offsetX += dragAmount

                                when {
                                    offsetX > 0f -> {
                                        if (dragAmount < 0f)
                                            offsetX += dragAmount
                                    }

                                    offsetX < 0f -> {
                                        if (dragAmount > 0f)
                                            offsetX += dragAmount
                                    }
                                }
                            },
                            onDragEnd = {
                                when {
                                    offsetX > positionalThreshold && enableSwipeStartToEnd -> {
                                        scope.launch {
                                            offsetX = exitScreenWidth
                                            onSwipeRight()
                                            delay(200)
                                            offsetX = 0f
                                        }
                                    }

                                    offsetX < -positionalThreshold && enableSwipeEndToStart -> {
                                        scope.launch {
                                            offsetX = -exitScreenWidth
                                            onSwipeLeft()
                                            delay(200)
                                            offsetX = 0f
                                        }
                                    }

                                    else -> offsetX = 0f
                                }
                            }
                        )
                }
                .testTag(UiTestTags.TASK_CONTENT),
            content = content
        )
    }
}
