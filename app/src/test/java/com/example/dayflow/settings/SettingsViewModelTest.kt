package com.example.dayflow.settings

import app.cash.turbine.test
import com.example.dayflow.data.local.data_store.DataStoreManager
import com.example.dayflow.ui.settings.vm.SettingsEvents
import com.example.dayflow.ui.settings.vm.SettingsViewModel
import com.example.dayflow.ui.utils.ContentStatus
import com.example.dayflow.utils.BaseViewModelTester
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SettingsViewModelTest : BaseViewModelTester() {

    private lateinit var dataStoreManager: DataStoreManager
    private lateinit var viewModel: SettingsViewModel

    override fun setUp() {
        dataStoreManager = mockk()
        viewModel = SettingsViewModel(dataStoreManager)
    }

    @Test
    fun `initial data when init viewModel then update content status to visible`() = runTest {
        viewModel.state.test {
            assertTrue(awaitItem().contentStatus == ContentStatus.LOADING)

            coEvery { dataStoreManager.isLightTheme } returns flow { emit(false) }

            val visibleState = awaitItem()
            assertTrue(visibleState.contentStatus == ContentStatus.VISIBLE)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `initial data when init viewModel then update theme state`() = runTest {
        viewModel.state.test {
            assertEquals(awaitItem().contentStatus, ContentStatus.LOADING)

            coEvery { dataStoreManager.isLightTheme } returns flow { emit(true) }

            val visibleState = awaitItem()
            assertEquals(visibleState.contentStatus, ContentStatus.VISIBLE)
            assertTrue(visibleState.isLightTheme)

            cancelAndIgnoreRemainingEvents()
        }
    }


    @Test
    fun `given navigation event when call onClickBlockApps() then send event`() = runTest {
        viewModel.events.test {

            viewModel.onClickBlockApps()

            val event = awaitItem()
            assertTrue(event is SettingsEvents.NavigateToBlockApps)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given toggle theme when call onToggleTheme() then update theme state`() = runTest {
        viewModel.state.test {
            assertEquals(awaitItem().contentStatus, ContentStatus.LOADING)

            coEvery { dataStoreManager.isLightTheme } returns flow { emit(false) }

            assertTrue(awaitItem().contentStatus == ContentStatus.VISIBLE)

            coEvery { dataStoreManager.toggleThemeStatus() } returns Unit

            viewModel.onToggleTheme()

            val toggleState = awaitItem()
            assertTrue(toggleState.isLightTheme)

            cancelAndIgnoreRemainingEvents()
        }
        coVerify { dataStoreManager.toggleThemeStatus() }
    }
}