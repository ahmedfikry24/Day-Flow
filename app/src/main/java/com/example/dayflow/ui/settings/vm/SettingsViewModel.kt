package com.example.dayflow.ui.settings.vm

import androidx.lifecycle.viewModelScope
import com.example.dayflow.data.local.data_store.DataStoreManager
import com.example.dayflow.ui.base.BaseViewModel
import com.example.dayflow.ui.utils.ContentStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val dataStoreManager: DataStoreManager
) : BaseViewModel<SettingsUiState, SettingsEvents>(SettingsUiState()), SettingsInteractions {

    override fun initData() {
        viewModelScope.launch {
            dataStoreManager.isLightTheme.collect { state ->
                _state.update {
                    it.copy(
                        contentStatus = ContentStatus.VISIBLE,
                        isLightTheme = state
                    )
                }
            }
        }
    }

    override fun onClickBlockApps() {
        sendEvent(SettingsEvents.NavigateToBlockApps)
    }

    override fun onToggleTheme() {
        _state.update { it.copy(isLightTheme = !it.isLightTheme) }
        viewModelScope.launch {
            dataStoreManager.toggleThemeStatus()
        }
    }
}
