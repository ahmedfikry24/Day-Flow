package com.example.dayflow.work_session

import app.cash.turbine.test
import com.example.dayflow.service.DefaultServiceManager
import com.example.dayflow.ui.utils.ContentStatus
import com.example.dayflow.ui.utils.convertSessionTimeToLong
import com.example.dayflow.ui.work_session.vm.WorkSessionViewModel
import com.example.dayflow.utils.BaseViewModelTester
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
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

    @Test
    fun `given start session when call startSession() then update state`() = runTest {
        viewModel.state.test {
            val initialState = awaitItem()
            assertTrue(initialState.isSessionInfoVisible)
            assertEquals(0L, initialState.sessionDuration)
            assertEquals(0L, initialState.sessionRemainingTime)
            assertFalse(initialState.isRunning)

            coEvery { serviceManager.createSessionService(any()) } returns Unit

            viewModel.startSession()

            val startSessionState = awaitItem()
            assertFalse(startSessionState.isSessionInfoVisible)
            assertEquals(
                initialState.sessionDurationMin.convertSessionTimeToLong(),
                startSessionState.sessionDuration
            )
            assertEquals(
                initialState.sessionDurationMin.convertSessionTimeToLong(),
                startSessionState.sessionRemainingTime
            )
            assertTrue(startSessionState.isRunning)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given start session when call startSession() then verify service started`() = runTest {
        viewModel.state.test {
            assertTrue(awaitItem().isSessionInfoVisible)

            coEvery { serviceManager.createSessionService(any()) } returns Unit

            viewModel.startSession()

            val startSessionState = awaitItem()
            assertFalse(startSessionState.isSessionInfoVisible)
            assertTrue(startSessionState.isRunning)

            cancelAndIgnoreRemainingEvents()
        }
        coVerify { serviceManager.createSessionService(any()) }
    }

    @Test
    fun `given remaining time when call onChangeSessionRemainingTime() then return right time`() =
        runTest {
            viewModel.state.test {
                assertTrue(awaitItem().isSessionInfoVisible)

                coEvery { serviceManager.createSessionService(any()) } returns Unit

                viewModel.startSession()
                val startSessionState = awaitItem()
                assertFalse(startSessionState.isSessionInfoVisible)

                viewModel.onChangeSessionRemainingTime()
                val updatedState = awaitItem()

                val expectedTime =
                    startSessionState.sessionDurationMin.convertSessionTimeToLong() - 1000L

                assertEquals(expectedTime, updatedState.sessionRemainingTime)

                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `given pause session time when call pauseSession() then stop session`() = runTest {
        viewModel.state.test {
            assertTrue(awaitItem().isSessionInfoVisible)

            coEvery { serviceManager.createSessionService(any()) } returns Unit

            viewModel.startSession()
            assertTrue(awaitItem().isRunning)

            coEvery { serviceManager.cancelSessionService() } returns Unit

            viewModel.pauseSession()

            assertFalse(awaitItem().isRunning)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given pause session time when call pauseSession() then stop service`() = runTest {
        viewModel.state.test {
            assertTrue(awaitItem().isSessionInfoVisible)

            coEvery { serviceManager.createSessionService(any()) } returns Unit

            viewModel.startSession()
            assertTrue(awaitItem().isRunning)

            coEvery { serviceManager.cancelSessionService() } returns Unit

            viewModel.pauseSession()

            cancelAndIgnoreRemainingEvents()
        }
        coVerify { serviceManager.cancelSessionService() }
    }
}