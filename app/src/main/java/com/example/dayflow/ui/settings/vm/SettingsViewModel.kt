package com.example.dayflow.ui.settings.vm

import com.example.dayflow.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor() :
    BaseViewModel<SettingsUiState, SettingsEvents>(SettingsUiState()), SettingsInteractions {

    override fun initData() {

    }

}
