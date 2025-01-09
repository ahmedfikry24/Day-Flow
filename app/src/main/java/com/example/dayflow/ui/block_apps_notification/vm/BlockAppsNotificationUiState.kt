package com.example.dayflow.ui.block_apps_notification.vm

import android.graphics.Bitmap
import com.example.dayflow.data.local.entity.BlockAppInfoEntity
import com.example.dayflow.ui.utils.ContentStatus

data class BlockAppsNotificationUiState(
    val contentStatus: ContentStatus = ContentStatus.LOADING,
    val appsInfo: List<BlockAppInfoUiState> = listOf()
) {
    data class BlockAppInfoUiState(
        val id: Int = 0,
        val name: String = "",
        val icon: Bitmap? = null,
        val isBlock: Boolean = false
    )
}

fun BlockAppInfoEntity.toUiState(): BlockAppsNotificationUiState.BlockAppInfoUiState {
    return BlockAppsNotificationUiState.BlockAppInfoUiState(
        id = this.id,
        name = this.name,
        icon = this.icon,
        isBlock = this.isBlock
    )
}


fun BlockAppsNotificationUiState.BlockAppInfoUiState.toEntity(): BlockAppInfoEntity {
    return BlockAppInfoEntity(
        id = this.id,
        name = this.name,
        icon = this.icon,
        isBlock = this.isBlock
    )
}