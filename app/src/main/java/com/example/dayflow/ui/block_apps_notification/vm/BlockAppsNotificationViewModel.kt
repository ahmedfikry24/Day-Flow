package com.example.dayflow.ui.block_apps_notification.vm

import com.example.dayflow.data.local.entity.BlockAppInfoEntity
import com.example.dayflow.data.repository.Repository
import com.example.dayflow.data.usecase.GetAllInstalledAppsUseCase
import com.example.dayflow.ui.base.BaseViewModel
import com.example.dayflow.ui.utils.ContentStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class BlockAppsNotificationViewModel @Inject constructor(
    private val getAllInstalledAppsUseCase: GetAllInstalledAppsUseCase,
    private val repository: Repository
) : BaseViewModel<BlockAppsNotificationUiState, BlockAppsNotificationEvents>(
    BlockAppsNotificationUiState()
), BlockAppsNotificationInteractions {

    override fun initData() {
        _state.update { it.copy(contentStatus = ContentStatus.LOADING) }
        tryExecute(
            { getAllInstalledAppsUseCase() },
            ::getAllInstalledAppsSuccess,
            ::setFailureContent
        )
    }

    private fun getAllInstalledAppsSuccess(apps: List<BlockAppInfoEntity>) {
        _state.update { value ->
            value.copy(
                contentStatus = ContentStatus.VISIBLE,
                appsInfo = apps.map { it.toUiState() }
            )
        }
    }

    private fun setFailureContent() {
        _state.update { it.copy(contentStatus = ContentStatus.FAILURE) }
    }


    override fun onClickBack() {
        sendEvent(BlockAppsNotificationEvents.NavigateToBack)
    }

    override fun onBlockApp(app: BlockAppsNotificationUiState.BlockAppInfoUiState) {
        tryExecute(
            { repository.addBlockedApp(app.toEntity().copy(isBlock = true)) },
            {
                val appIndex = state.value.appsInfo.indexOf(app)
                _state.update {
                    it.copy(
                        appsInfo = it.appsInfo.toMutableList().apply {
                            this[appIndex] = this[appIndex].copy(isBlock = !this[appIndex].isBlock)
                        }
                    )
                }
            },
            ::setFailureContent
        )
    }

    override fun onRemoveBlockedApp(app: BlockAppsNotificationUiState.BlockAppInfoUiState) {
        tryExecute(
            { repository.removeBlockedApp(app.toEntity().id) },
            {
                val appIndex = state.value.appsInfo.indexOf(app)
                _state.update {
                    it.copy(
                        appsInfo = it.appsInfo.toMutableList().apply {
                            this[appIndex] = this[appIndex].copy(isBlock = !this[appIndex].isBlock)
                        }
                    )
                }
            },
            ::setFailureContent
        )
    }
}
