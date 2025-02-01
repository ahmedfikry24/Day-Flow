package com.example.dayflow.block_apps_notification

import app.cash.turbine.test
import com.example.dayflow.data.usecase.GetAllInstalledAppsUseCase
import com.example.dayflow.data.utils.PackageAppsManager
import com.example.dayflow.ui.block_apps_notification.vm.BlockAppsNotificationViewModel
import com.example.dayflow.ui.utils.ContentStatus
import com.example.dayflow.utils.BaseViewModelTester
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class BlockAppsNotificationViewModelTest : BaseViewModelTester() {

    private lateinit var packageAppsManager: PackageAppsManager
    private lateinit var getAllInstalledAppsUseCase: GetAllInstalledAppsUseCase
    private lateinit var viewModel: BlockAppsNotificationViewModel

    override fun setUp() {
        super.setUp()
        packageAppsManager = mockk()
        getAllInstalledAppsUseCase = GetAllInstalledAppsUseCase(spyRepository, packageAppsManager)
        viewModel = BlockAppsNotificationViewModel(getAllInstalledAppsUseCase, repository)
    }

    @Test
    fun `initial data when init viewModel then update state`() = runTest {
        viewModel.state.test {
            assertEquals(ContentStatus.LOADING, awaitItem().contentStatus)

            coEvery { packageAppsManager.packageManager } returns mockk()
            coEvery { packageAppsManager.getAllInstalledApps() } returns emptyList()

            val visibleState = awaitItem()
            assertEquals(visibleState.contentStatus, ContentStatus.VISIBLE)
            assertTrue(visibleState.appsInfo.isEmpty())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `initial data when init viewModel then verify getAllBlockApps`() = runTest {
        viewModel.state.test {
            assertEquals(ContentStatus.LOADING, awaitItem().contentStatus)

            coEvery { packageAppsManager.packageManager } returns mockk()
            coEvery { packageAppsManager.getAllInstalledApps() } returns emptyList()

            assertEquals(awaitItem().contentStatus, ContentStatus.VISIBLE)

            cancelAndIgnoreRemainingEvents()
        }
        coVerify { spyRepository.getAllBlockedApps() }
    }

    @Test
    fun `throw exception when init viewModel then update status to failure`() = runTest {
        viewModel.state.test {
            assertEquals(ContentStatus.LOADING, awaitItem().contentStatus)

            coEvery { packageAppsManager.packageManager } returns mockk()
            coEvery { packageAppsManager.getAllInstalledApps() } returns emptyList()
            coEvery { spyRepository.getAllBlockedApps() } throws Exception("Error")

            assertEquals(awaitItem().contentStatus, ContentStatus.FAILURE)

            cancelAndIgnoreRemainingEvents()
        }
        coVerify { spyRepository.getAllBlockedApps() }
    }
}