package com.example.dayflow.yearly_tasks

import app.cash.turbine.test
import com.example.dayflow.data.local.entity.YearlyTaskEntity
import com.example.dayflow.data.usecase.AddYearlyTaskUseCase
import com.example.dayflow.data.usecase.DeleteYearlyTaskUseCase
import com.example.dayflow.data.usecase.GetAllYearlyTasksUseCase
import com.example.dayflow.ui.utils.ContentStatus
import com.example.dayflow.ui.utils.UiConstants
import com.example.dayflow.ui.utils.ui_state.AddTaskUiState
import com.example.dayflow.ui.utils.ui_state.toUiState
import com.example.dayflow.ui.yearly_tasks.vm.YearlyTasksViewModel
import com.example.dayflow.utils.BaseViewModelTester
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class YearlyTasksViewModelTest : BaseViewModelTester() {

    private lateinit var getAllYearlyTasksUseCase: GetAllYearlyTasksUseCase
    private lateinit var addYearlyTasksUseCase: AddYearlyTaskUseCase
    private lateinit var deleteYearlyTaskUseCase: DeleteYearlyTaskUseCase
    private lateinit var viewModel: YearlyTasksViewModel

    override fun setUp() {
        super.setUp()
        getAllYearlyTasksUseCase = GetAllYearlyTasksUseCase(spyRepository)
        addYearlyTasksUseCase = AddYearlyTaskUseCase(spyRepository)
        deleteYearlyTaskUseCase = DeleteYearlyTaskUseCase(spyRepository)
        viewModel = YearlyTasksViewModel(
            getAllYearlyTasksUseCase,
            addYearlyTasksUseCase,
            deleteYearlyTaskUseCase
        )
    }


    @Test
    fun `given call initData() when start collect state then update status to visible`() = runTest {
        viewModel.state.test {
            assertEquals(ContentStatus.LOADING, awaitItem().contentStatus)
            assertEquals(ContentStatus.VISIBLE, awaitItem().contentStatus)
            cancelAndIgnoreRemainingEvents()
        }
        coVerify { spyRepository.getAllYearlyTasks() }
    }

    @Test
    fun `given get all tasks when call initData() then return valid list`() = runTest {
        val expectedTasks = repository.getAllYearlyTasks().map { it.toUiState() }
        viewModel.state.test {
            assertEquals(ContentStatus.LOADING, awaitItem().contentStatus)

            val visibleState = awaitItem()
            assertEquals(ContentStatus.VISIBLE, visibleState.contentStatus)
            assertEquals(expectedTasks, visibleState.tasks)

            cancelAndIgnoreRemainingEvents()
        }
        coVerify { spyRepository.getAllYearlyTasks() }
    }

    @Test
    fun `given get all tasks when call initData() then update last task id`() = runTest {
        val task = YearlyTaskEntity(
            id = 1,
            title = "ahmed",
            description = "",
        )
        repository.addYearlyTask(task)

        viewModel.state.test {
            assertEquals(ContentStatus.LOADING, awaitItem().contentStatus)

            val visibleState = awaitItem()
            assertEquals(ContentStatus.VISIBLE, visibleState.contentStatus)

            assertEquals(task.id + 1, UiConstants.lastYearlyTaskId)

            cancelAndIgnoreRemainingEvents()
        }
        coVerify { spyRepository.getAllYearlyTasks() }
    }

    @Test
    fun `given exception when get all tasks then update status to failure`() = runTest {
        coEvery { spyRepository.getAllYearlyTasks() } throws Exception("Error")
        viewModel.state.test {
            assertEquals(ContentStatus.LOADING, awaitItem().contentStatus)

            val failureState = awaitItem()
            assertEquals(ContentStatus.FAILURE, failureState.contentStatus)
            cancelAndIgnoreRemainingEvents()
        }
        coVerify { spyRepository.getAllYearlyTasks() }
    }

    @Test
    fun `given control add task visibility when add task pressed then update state`() = runTest {
        viewModel.state.test {

            assertEquals(ContentStatus.LOADING, awaitItem().contentStatus)
            val visibleState = awaitItem()
            assertEquals(ContentStatus.VISIBLE, visibleState.contentStatus)

            viewModel.controlAddTaskVisibility()
            val openAddTaskState = awaitItem()
            assertTrue(visibleState.isAddTaskVisible != openAddTaskState.isAddTaskVisible)

            viewModel.controlAddTaskVisibility()
            val closeAddTaskState = awaitItem()
            assertTrue(openAddTaskState.isAddTaskVisible != closeAddTaskState.isAddTaskVisible)
            cancelAndIgnoreRemainingEvents()
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
            cancelAndIgnoreRemainingEvents()
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
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `given empty title when call addTask() then don't add task`() = runTest {
        viewModel.state.test {
            assertEquals(ContentStatus.LOADING, awaitItem().contentStatus)
            assertEquals(ContentStatus.VISIBLE, awaitItem().contentStatus)

            val title = ""
            viewModel.onTitleChange(title)

            viewModel.addTask()
            assertTrue(awaitItem().addTask.titleError)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify(atMost = 0, atLeast = 0) { spyRepository.addYearlyTask(any()) }
    }

    @Test
    fun `given valid task info when call addTask() then add task success`() = runTest {
        viewModel.state.test {
            assertEquals(ContentStatus.LOADING, awaitItem().contentStatus)
            assertEquals(ContentStatus.VISIBLE, awaitItem().contentStatus)

            val title = "ahmed"
            viewModel.onTitleChange(title)
            awaitItem()

            val description = "ahmed"
            viewModel.onDescriptionChange(description)
            awaitItem()

            viewModel.addTask()
            val loadingState = awaitItem()

            val successState = awaitItem()
            assertEquals(ContentStatus.VISIBLE, successState.contentStatus)
            assertTrue(successState.tasks.contains(loadingState.addTask.toUiState(UiConstants.lastYearlyTaskId - 1)))
            assertEquals(AddTaskUiState(), successState.addTask)

            val tasks = repository.getAllYearlyTasks().map { it.toUiState() }
            assertEquals(tasks, successState.tasks)
            assertTrue(tasks.maxBy { it.id }.id < UiConstants.lastYearlyTaskId)

            cancelAndIgnoreRemainingEvents()
        }
        coVerify { spyRepository.addYearlyTask(any()) }
    }

    @Test
    fun `given task id  when onSwipeDeleteTask() then update selected item id`() = runTest {
        val task = YearlyTaskEntity(
            id = 1,
            title = "ahmed",
            description = "",
        )
        viewModel.state.test {
            assertEquals(ContentStatus.LOADING, awaitItem().contentStatus)
            assertEquals(ContentStatus.VISIBLE, awaitItem().contentStatus)

            viewModel.onSwipeDeleteTask(task.id)
            assertEquals(task.id, awaitItem().selectedDeleteItemId)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given control delete task dialog visibility when delete task then update state`() =
        runTest {
            viewModel.state.test {

                assertEquals(ContentStatus.LOADING, awaitItem().contentStatus)
                val visibleState = awaitItem()
                assertEquals(ContentStatus.VISIBLE, visibleState.contentStatus)

                viewModel.controlDeleteItemDialogVisibility()
                val openDeleteTaskState = awaitItem()
                assertTrue(visibleState.isDeleteTaskDialogVisible != openDeleteTaskState.isDeleteTaskDialogVisible)

                viewModel.controlDeleteItemDialogVisibility()
                val closeDeleteTaskState = awaitItem()
                assertTrue(openDeleteTaskState.isDeleteTaskDialogVisible != closeDeleteTaskState.isDeleteTaskDialogVisible)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `given task when deleteTask() then delete it success`() = runTest {
        val task = YearlyTaskEntity(
            id = 1,
            title = "ahmed",
            description = "",
        )
        repository.addYearlyTask(task)

        viewModel.state.test {

            assertEquals(ContentStatus.LOADING, awaitItem().contentStatus)
            assertEquals(ContentStatus.VISIBLE, awaitItem().contentStatus)


            viewModel.onSwipeDeleteTask(task.id)
            awaitItem()

            viewModel.deleteTask()
            val successState = awaitItem()

            assertEquals(ContentStatus.VISIBLE, successState.contentStatus)
            assertTrue(successState.tasks.none { it == task.toUiState() })

            val tasks = repository.getAllYearlyTasks().map { it.toUiState() }
            assertEquals(tasks, successState.tasks)

            cancelAndIgnoreRemainingEvents()
        }
        coVerify { spyRepository.deleteYearlyTask(task.id) }
    }

}