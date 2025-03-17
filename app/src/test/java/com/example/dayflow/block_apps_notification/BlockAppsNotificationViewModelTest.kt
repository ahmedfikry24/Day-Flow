package com.example.dayflow.block_apps_notification

import app.cash.turbine.test
import com.example.dayflow.data.local.entity.BlockAppInfoEntity
import com.example.dayflow.data.usecase.GetAllInstalledAppsUseCase
import com.example.dayflow.ui.block_apps_notification.vm.BlockAppsNotificationEvents
import com.example.dayflow.ui.block_apps_notification.vm.BlockAppsNotificationUiState
import com.example.dayflow.ui.block_apps_notification.vm.BlockAppsNotificationViewModel
import com.example.dayflow.ui.utils.ContentStatus
import com.example.dayflow.utils.BaseViewModelTester
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
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
        viewModel = BlockAppsNotificationViewModel(getAllInstalledAppsUseCase, spyRepository)
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

    @Test
    fun `given navigation event when call onClickBack() then send event`() = runTest {
        viewModel.events.test {

            viewModel.onClickBack()

            val event = awaitItem()
            assertTrue(event is BlockAppsNotificationEvents.NavigateToBack)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given permission dialog visibility when call controlNotificationAccessDialogVisibility() then update state`() =
        runTest {
            viewModel.state.test {
                assertEquals(ContentStatus.LOADING, awaitItem().contentStatus)

                coEvery { packageAppsManager.packageManager } returns mockk()
                coEvery { packageAppsManager.getAllInstalledApps() } returns emptyList()

                val visibleState = awaitItem()
                assertEquals(visibleState.contentStatus, ContentStatus.VISIBLE)
                assertFalse(visibleState.isNotificationAccessDialogVisible)

                viewModel.controlNotificationAccessDialogVisibility()
                assertTrue(awaitItem().isNotificationAccessDialogVisible)

                viewModel.controlNotificationAccessDialogVisibility()
                assertFalse(awaitItem().isNotificationAccessDialogVisible)

                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `given app info ui state when call onBlockApp() then update app state`() = runTest {
        val spyGetAppsUseCase = spyk(getAllInstalledAppsUseCase)
        viewModel = BlockAppsNotificationViewModel(spyGetAppsUseCase, spyRepository)
        viewModel.state.test {
            assertEquals(awaitItem().contentStatus, ContentStatus.LOADING)

            coEvery { packageAppsManager.packageManager } returns mockk()
            coEvery { packageAppsManager.getAllInstalledApps() } returns emptyList()
            coEvery { spyGetAppsUseCase.invoke() } returns listOf(
                BlockAppInfoEntity(
                    id = 1,
                    name = "app name",
                    packageName = "com.app.package",
                    icon = null,
                    isBlock = false
                )
            )

            val visibleState = awaitItem()
            assertEquals(visibleState.contentStatus, ContentStatus.VISIBLE)
            assertTrue(visibleState.appsInfo.isNotEmpty())


            viewModel.onBlockApp(visibleState.appsInfo.first())

            assertTrue(awaitItem().appsInfo.first().isBlock)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given app info ui state when call onBlockApp() then verify app added`() = runTest {
        viewModel.state.test {
            assertEquals(awaitItem().contentStatus, ContentStatus.LOADING)

            coEvery { packageAppsManager.packageManager } returns mockk()
            coEvery { packageAppsManager.getAllInstalledApps() } returns emptyList()

            assertEquals(awaitItem().contentStatus, ContentStatus.VISIBLE)

            val appInfo = BlockAppsNotificationUiState.BlockAppInfoUiState(
                id = 1,
                name = "app name",
                packageName = "com.app.package",
                icon = null
            )
            viewModel.onBlockApp(appInfo)

            cancelAndIgnoreRemainingEvents()
        }
        coVerify { spyRepository.addBlockedApp(any()) }
    }

    @Test
    fun `given app info ui state when call onRemoveBlockedApp() then update app state`() = runTest {
        val spyGetAppsUseCase = spyk(getAllInstalledAppsUseCase)
        viewModel = BlockAppsNotificationViewModel(spyGetAppsUseCase, spyRepository)
        viewModel.state.test {
            assertEquals(awaitItem().contentStatus, ContentStatus.LOADING)

            coEvery { packageAppsManager.packageManager } returns mockk()
            coEvery { packageAppsManager.getAllInstalledApps() } returns emptyList()
            coEvery { spyGetAppsUseCase.invoke() } returns listOf(
                BlockAppInfoEntity(
                    id = 1,
                    name = "app name",
                    packageName = "com.app.package",
                    icon = null,
                    isBlock = true
                )
            )

            val visibleState = awaitItem()
            assertEquals(visibleState.contentStatus, ContentStatus.VISIBLE)
            assertTrue(visibleState.appsInfo.isNotEmpty())


            viewModel.onRemoveBlockedApp(visibleState.appsInfo.first())

            assertFalse(awaitItem().appsInfo.first().isBlock)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given app info ui state when call onRemoveBlockedApp() then verify app added`() = runTest {
        viewModel.state.test {
            assertEquals(awaitItem().contentStatus, ContentStatus.LOADING)

            coEvery { packageAppsManager.packageManager } returns mockk()
            coEvery { packageAppsManager.getAllInstalledApps() } returns emptyList()

            assertEquals(awaitItem().contentStatus, ContentStatus.VISIBLE)

            val appInfo = BlockAppsNotificationUiState.BlockAppInfoUiState(
                id = 1,
                name = "app name",
                packageName = "com.app.package",
                icon = null
            )
            viewModel.onRemoveBlockedApp(appInfo)

            cancelAndIgnoreRemainingEvents()
        }
        coVerify { spyRepository.removeBlockedApp(any()) }
    }
}