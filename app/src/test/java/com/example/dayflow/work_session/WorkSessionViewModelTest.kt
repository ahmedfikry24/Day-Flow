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
                assertTrue(awaitItem().isSessionInfoVisible)

                viewModel.controlSessionInfoVisibility()
                assertFalse(awaitItem().isSessionInfoVisible)

                viewModel.controlSessionInfoVisibility()
                assertTrue(awaitItem().isSessionInfoVisible)

                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `given plus session duration when call plusSessionDurationMin() then update state`() =
        runTest {
            viewModel.state.test {
                val visibleState = awaitItem()
                assertEquals(ContentStatus.VISIBLE, visibleState.contentStatus)

                viewModel.plusSessionDurationMin()
                assertEquals(visibleState.sessionDurationMin + 5, awaitItem().sessionDurationMin)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `given minus session duration when call minusSessionDurationMin() then update state`() =
        runTest {
            viewModel.state.test {
                val visibleState = awaitItem()
                assertEquals(ContentStatus.VISIBLE, visibleState.contentStatus)

                viewModel.minusSessionDurationMin()
                assertEquals(visibleState.sessionDurationMin - 5, awaitItem().sessionDurationMin)
                cancelAndIgnoreRemainingEvents()
            }
        }
}