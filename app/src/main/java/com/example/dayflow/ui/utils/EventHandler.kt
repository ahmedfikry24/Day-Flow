package com.example.dayflow.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow


@Composable
fun <T> EventHandler(
    effects: SharedFlow<T>,
    handleEffect: (T, CoroutineScope) -> Unit
) {
    LaunchedEffect(key1 = Unit) {
        effects.throttleFirst().collectLatest { effect ->
            handleEffect(effect, this)
        }
    }
}

private fun <T> SharedFlow<T>.throttleFirst(): Flow<T> {
    return flow {
        var lastEmitted = 0L
        collect { event ->
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastEmitted >= 500) {
                lastEmitted = currentTime
                emit(event)
            }
        }
    }
}
