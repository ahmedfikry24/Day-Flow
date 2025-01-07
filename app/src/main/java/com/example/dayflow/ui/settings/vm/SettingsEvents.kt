package com.example.dayflow.ui.settings.vm

sealed interface SettingsEvents {
    data object NavigateToBlockApps : SettingsEvents
}
