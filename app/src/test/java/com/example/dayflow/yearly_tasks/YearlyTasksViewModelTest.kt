package com.example.dayflow.yearly_tasks

import app.cash.turbine.test
import com.example.dayflow.data.usecase.AddYearlyTaskUseCase
import com.example.dayflow.data.usecase.DeleteYearlyTaskUseCase
import com.example.dayflow.data.usecase.GetAllYearlyTasksUseCase
import com.example.dayflow.ui.utils.ContentStatus
import com.example.dayflow.ui.utils.ui_state.toUiState
import com.example.dayflow.ui.yearly_tasks.vm.YearlyTasksViewModel
import com.example.dayflow.utils.BaseViewModelTester
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
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

}