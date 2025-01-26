package com.example.dayflow.daily_tasks

import app.cash.turbine.test
import com.example.dayflow.data.alarm.DefaultAlarmManager
import com.example.dayflow.data.local.entity.DailyTaskEntity
import com.example.dayflow.data.usecase.AddDailyTaskUseCase
import com.example.dayflow.data.usecase.DeleteDailyTaskUseCase
import com.example.dayflow.data.usecase.GetAllDailyTasksUseCase
import com.example.dayflow.data.usecase.UpdateDailyTaskStatusUseCase
import com.example.dayflow.repository.FakeRepository
import com.example.dayflow.ui.daily_tasks.vm.DailyTasksViewModel
import com.example.dayflow.ui.utils.ContentStatus
import com.example.dayflow.ui.utils.UiConstants
import com.example.dayflow.ui.utils.ui_state.toUiState
import com.example.dayflow.utils.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class DailyTaskViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var repository: FakeRepository
    private lateinit var spyRepository: FakeRepository
    private lateinit var getAllDailyTasksUseCase: GetAllDailyTasksUseCase
    private lateinit var defaultAlarmManager: DefaultAlarmManager
    private lateinit var addDailyTasksUseCase: AddDailyTaskUseCase
    private lateinit var updateDailyTaskStatusUseCase: UpdateDailyTaskStatusUseCase
    private lateinit var deleteDailyTaskUseCase: DeleteDailyTaskUseCase
    private lateinit var viewModel: DailyTasksViewModel

    @Before
    fun setUp() {
        repository = FakeRepository()
        spyRepository = spyk(repository)
        getAllDailyTasksUseCase = GetAllDailyTasksUseCase(spyRepository)
        defaultAlarmManager = mockk()
        addDailyTasksUseCase = AddDailyTaskUseCase(spyRepository, defaultAlarmManager)
        updateDailyTaskStatusUseCase =
            UpdateDailyTaskStatusUseCase(spyRepository, defaultAlarmManager)
        deleteDailyTaskUseCase = DeleteDailyTaskUseCase(spyRepository, defaultAlarmManager)
        viewModel = DailyTasksViewModel(
            getAllDailyTasksUseCase,
            addDailyTasksUseCase,
            updateDailyTaskStatusUseCase,
            deleteDailyTaskUseCase
        )
    }

    @Test
    fun `call initData() when init viewModel then update content status to visible`() = runTest {

        viewModel.state.test {
            val loadingState = awaitItem()
            assertEquals(ContentStatus.LOADING, loadingState.contentStatus)

            val visibleState = awaitItem()
            assertEquals(ContentStatus.VISIBLE, visibleState.contentStatus)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify { spyRepository.getAllDailyTasks() }
    }

    @Test
    fun `get all tasks when call initData() then return valid list`() = runTest {
        val tasks = repository.getAllDailyTasks().map { it.toUiState() }
        val expectedInProgressTasks = tasks.filter { !it.isDone }
        val expectedDoneTasks = tasks.filter { it.isDone }

        viewModel.state.test {
            val loadingState = awaitItem()
            assertEquals(ContentStatus.LOADING, loadingState.contentStatus)

            val visibleState = awaitItem()
            assertEquals(expectedInProgressTasks, visibleState.inProgressTasks)
            assertEquals(expectedDoneTasks, visibleState.doneTasks)

            cancelAndIgnoreRemainingEvents()
        }

        coVerify(atMost = 1) { spyRepository.getAllDailyTasks() }
    }

    @Test
    fun `throw exception when call initData() then update content status to failure`() = runTest {
        val message = "error message"
        coEvery { spyRepository.getAllDailyTasks() } throws Exception(message)

        viewModel.state.test {
            val loadingState = awaitItem()
            assertEquals(ContentStatus.LOADING, loadingState.contentStatus)

            val visibleState = awaitItem()
            assertEquals(ContentStatus.FAILURE, visibleState.contentStatus)

            cancelAndIgnoreRemainingEvents()
        }

        coVerify { spyRepository.getAllDailyTasks() }
    }

    @Test
    fun `given tasks entity when call initData() then update last task id`() = runTest {
        repository.getAllDailyTasks()
        coEvery { spyRepository.getAllDailyTasks() } coAnswers {
            listOf(
                DailyTaskEntity(
                    id = 1,
                    title = "",
                    description = "",
                    date = null,
                    time = null,
                    status = false
                )
            )
        }

        viewModel.state.test {
            val loadingState = awaitItem()
            assertEquals(ContentStatus.LOADING, loadingState.contentStatus)

            val visibleState = awaitItem()
            assertEquals(ContentStatus.VISIBLE, visibleState.contentStatus)
            assertEquals(2, UiConstants.lastDailyTaskId)

            cancelAndIgnoreRemainingEvents()
        }

        coVerify(atMost = 1) { spyRepository.getAllDailyTasks() }
    }

}