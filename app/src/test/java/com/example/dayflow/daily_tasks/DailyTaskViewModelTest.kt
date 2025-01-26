package com.example.dayflow.daily_tasks

import app.cash.turbine.test
import com.example.dayflow.data.alarm.DefaultAlarmManager
import com.example.dayflow.data.local.entity.DailyTaskEntity
import com.example.dayflow.data.usecase.AddDailyTaskUseCase
import com.example.dayflow.data.usecase.DeleteDailyTaskUseCase
import com.example.dayflow.data.usecase.GetAllDailyTasksUseCase
import com.example.dayflow.data.usecase.UpdateDailyTaskStatusUseCase
import com.example.dayflow.ui.daily_tasks.vm.DailyTasksViewModel
import com.example.dayflow.ui.utils.ContentStatus
import com.example.dayflow.ui.utils.UiConstants
import com.example.dayflow.ui.utils.ui_state.toUiState
import com.example.dayflow.utils.BaseViewModelTester
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test


class DailyTaskViewModelTest : BaseViewModelTester() {

    private lateinit var getAllDailyTasksUseCase: GetAllDailyTasksUseCase
    private lateinit var defaultAlarmManager: DefaultAlarmManager
    private lateinit var addDailyTasksUseCase: AddDailyTaskUseCase
    private lateinit var updateDailyTaskStatusUseCase: UpdateDailyTaskStatusUseCase
    private lateinit var deleteDailyTaskUseCase: DeleteDailyTaskUseCase
    private lateinit var viewModel: DailyTasksViewModel

    override fun setUp() {
        super.setUp()
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

    @Test
    fun `control add task content visibility when add task pressed then update state`() = runTest {
        viewModel.state.test {

            val loadingState = awaitItem()
            assertEquals(ContentStatus.LOADING, loadingState.contentStatus)

            viewModel.controlAddTaskVisibility()
            val openAddTaskState = awaitItem()
            assertTrue(loadingState.isAddTaskVisible != openAddTaskState.isAddTaskVisible)

            viewModel.controlAddTaskVisibility()
            val closeAddTaskState = awaitItem()
            assertTrue(openAddTaskState.isAddTaskVisible != closeAddTaskState.isAddTaskVisible)
        }
    }

    @Test
    fun `given string title when onTitleChange() then update add task state`() = runTest {
        viewModel.state.test {
            val loadingState = awaitItem()
            assertEquals(ContentStatus.LOADING, loadingState.contentStatus)

            val title = "ahmed"
            viewModel.onTitleChange(title)

            val state = awaitItem()
            assertEquals(title, state.addTask.title)
        }
    }

    @Test
    fun `given string description when onDescriptionChange() then update add task state`() =
        runTest {
            viewModel.state.test {
                val loadingState = awaitItem()
                assertEquals(ContentStatus.LOADING, loadingState.contentStatus)

                val description = "ahmed"
                viewModel.onDescriptionChange(description)

                val updatedState = awaitItem()
                assertEquals(description, updatedState.addTask.description)
            }
        }

    @Test
    fun `given string date when onDateChange() then update add task state`() = runTest {
        viewModel.state.test {
            val loadingState = awaitItem()
            assertEquals(ContentStatus.LOADING, loadingState.contentStatus)

            val date = "2023-07-16"
            viewModel.onDateChange(date)

            val updatedState = awaitItem()
            assertEquals(date, updatedState.addTask.date)
        }
    }

    @Test
    fun `given string time when onTimeChange() then update add task state`() = runTest {
        viewModel.state.test {
            val loadingState = awaitItem()
            assertEquals(ContentStatus.LOADING, loadingState.contentStatus)

            val time = "16:30"
            viewModel.onTimeChange(time)

            val updatedState = awaitItem()
            assertEquals(time, updatedState.addTask.time)
        }
    }

    @Test
    fun `control alarm permission dialog visibility when change time or date then update state`() =
        runTest {
            viewModel.state.test {

                val loadingState = awaitItem()
                assertEquals(ContentStatus.LOADING, loadingState.contentStatus)

                viewModel.controlScheduleAlarmDialogVisibility()
                val openAlarmPermissionState = awaitItem()
                assertTrue(loadingState.addTask.canScheduleAlarmDialogVisibility != openAlarmPermissionState.addTask.canScheduleAlarmDialogVisibility)

                viewModel.controlScheduleAlarmDialogVisibility()
                val closePermissionState = awaitItem()
                assertTrue(openAlarmPermissionState.addTask.canScheduleAlarmDialogVisibility != closePermissionState.addTask.canScheduleAlarmDialogVisibility)
            }
        }

    @Test
    fun `control empty scheduling field dialog visibility when try add task then update state`() =
        runTest {
            viewModel.state.test {

                val loadingState = awaitItem()
                assertEquals(ContentStatus.LOADING, loadingState.contentStatus)

                viewModel.controlEmptySchedulingDialogVisibility()
                val openEmptySchedulingState = awaitItem()
                assertTrue(loadingState.addTask.isSchedulingEmptyDialogVisibility != openEmptySchedulingState.addTask.isSchedulingEmptyDialogVisibility)

                viewModel.controlEmptySchedulingDialogVisibility()
                val closeEmptySchedulingState = awaitItem()
                assertTrue(openEmptySchedulingState.addTask.isSchedulingEmptyDialogVisibility != closeEmptySchedulingState.addTask.isSchedulingEmptyDialogVisibility)
            }
        }

    @Test
    fun `control unValid scheduling dialog visibility when try add task then update state`() =
        runTest {
            viewModel.state.test {

                val loadingState = awaitItem()
                assertEquals(ContentStatus.LOADING, loadingState.contentStatus)

                viewModel.controlUnValidScheduledDialogVisibility()
                val openUnValidSchedulingState = awaitItem()
                assertTrue(loadingState.addTask.isScheduledUnValid != openUnValidSchedulingState.addTask.isScheduledUnValid)

                viewModel.controlUnValidScheduledDialogVisibility()
                val closeEUnValidSchedulingState = awaitItem()
                assertTrue(openUnValidSchedulingState.addTask.isScheduledUnValid != closeEUnValidSchedulingState.addTask.isScheduledUnValid)
            }
        }
}