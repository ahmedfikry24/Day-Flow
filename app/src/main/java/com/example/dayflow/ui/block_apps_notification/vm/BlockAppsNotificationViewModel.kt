package com.example.dayflow.ui.block_apps_notification.vm

import com.example.dayflow.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BlockAppsNotificationViewModel @Inject constructor() :
    BaseViewModel<BlockAppsNotificationUiState, BlockAppsNotificationEvents>(
        BlockAppsNotificationUiState()
    ), BlockAppsNotificationInteractions {

    override fun initData() {

    }

}
