package com.example.dayflow.ui.settings.vm

import com.example.dayflow.ui.utils.ContentStatus

data class SettingsUiState(
    val contentStatus: ContentStatus = ContentStatus.LOADING,
    val isLightTheme: Boolean = true
) 
