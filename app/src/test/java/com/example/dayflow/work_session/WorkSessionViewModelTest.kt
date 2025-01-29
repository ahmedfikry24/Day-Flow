package com.example.dayflow.work_session

import app.cash.turbine.test
import com.example.dayflow.service.DefaultServiceManager
import com.example.dayflow.ui.utils.ContentStatus
import com.example.dayflow.ui.work_session.vm.WorkSessionViewModel
import com.example.dayflow.utils.BaseViewModelTester
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class WorkSessionViewModelTest : BaseViewModelTester() {

    private lateinit var serviceManager: DefaultServiceManager
    private lateinit var viewModel: WorkSessionViewModel

    override fun setUp() {
        super.setUp()
        serviceManager = mockk()
        viewModel = WorkSessionViewModel(serviceManager)
    }

    @Test
    fun `given control session info visibility when start session pressed then update state`() =
        runTest {
            viewModel.state.test {
                val visibleState = awaitItem()
                assertEquals(ContentStatus.VISIBLE, visibleState.contentStatus)

                viewModel.controlSessionInfoVisibility()
                val openSessionCountDown = awaitItem()
                assertTrue(visibleState.isSessionInfoVisible != openSessionCountDown.isSessionInfoVisible)

                viewModel.controlSessionInfoVisibility()
                assertTrue(openSessionCountDown.isSessionInfoVisible != awaitItem().isSessionInfoVisible)
                cancelAndIgnoreRemainingEvents()
            }
        }
}