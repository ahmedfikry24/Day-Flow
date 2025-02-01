package com.example.dayflow.settings

import app.cash.turbine.test
import com.example.dayflow.data.local.data_store.DataStoreManager
import com.example.dayflow.ui.settings.vm.SettingsViewModel
import com.example.dayflow.ui.utils.ContentStatus
import com.example.dayflow.utils.BaseViewModelTester
import io.mockk.coEvery
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
}