package com.example.dayflow.ui.utils

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier


fun Modifier.applyPadding(
    state: Boolean,
    values: PaddingValues,
): Modifier {
    return if (state) this.padding(values) else this
}