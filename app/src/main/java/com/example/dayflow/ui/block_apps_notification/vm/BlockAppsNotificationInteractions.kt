package com.example.dayflow.ui.block_apps_notification.vm

interface BlockAppsNotificationInteractions {
    fun initData()
    fun onClickBack()
    fun controlNotificationAccessDialogVisibility()
    fun onBlockApp(app: BlockAppsNotificationUiState.BlockAppInfoUiState)
    fun onRemoveBlockedApp(app: BlockAppsNotificationUiState.BlockAppInfoUiState)
}
