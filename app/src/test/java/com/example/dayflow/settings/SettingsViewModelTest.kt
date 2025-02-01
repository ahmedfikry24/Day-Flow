package com.example.dayflow.settings

import com.example.dayflow.data.local.data_store.DataStoreManager
import com.example.dayflow.ui.settings.vm.SettingsViewModel
import com.example.dayflow.utils.BaseViewModelTester
import io.mockk.mockk

class SettingsViewModelTest : BaseViewModelTester() {

    private lateinit var dataStoreManager: DataStoreManager
    private lateinit var viewModel: SettingsViewModel

    override fun setUp() {
        dataStoreManager = mockk()
        viewModel = SettingsViewModel(dataStoreManager)
    }

}