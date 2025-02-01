package com.example.dayflow.block_apps_notification

import com.example.dayflow.data.usecase.GetAllInstalledAppsUseCase
import com.example.dayflow.ui.block_apps_notification.vm.BlockAppsNotificationViewModel
import com.example.dayflow.utils.BaseViewModelTester
import io.mockk.mockk

class BlockAppsNotificationViewModelTest : BaseViewModelTester() {

    private lateinit var getAllInstalledAppsUseCase: GetAllInstalledAppsUseCase
    private lateinit var viewModel: BlockAppsNotificationViewModel

    override fun setUp() {
        super.setUp()
        getAllInstalledAppsUseCase = mockk()
        viewModel = BlockAppsNotificationViewModel(getAllInstalledAppsUseCase, repository)
    }
}