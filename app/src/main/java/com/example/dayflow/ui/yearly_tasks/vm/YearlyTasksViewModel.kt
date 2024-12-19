package com.example.dayflow.ui.yearly_tasks.vm

import com.example.dayflow.data.local.entity.YearlyTaskEntity
import com.example.dayflow.data.usecase.AddYearlyTaskUseCase
import com.example.dayflow.data.usecase.DeleteYearlyTaskUseCase
import com.example.dayflow.data.usecase.GetAllYearlyTasksUseCase
import com.example.dayflow.ui.base.BaseViewModel
import com.example.dayflow.ui.utils.ContentStatus
import com.example.dayflow.ui.utils.generateRandomId
import com.example.dayflow.ui.utils.ui_state.AddTaskUiState
import com.example.dayflow.ui.utils.ui_state.toUiState
import com.example.dayflow.ui.utils.ui_state.toYearlyEntity
import com.example.dayflow.ui.utils.validateRequireField
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class YearlyTasksViewModel @Inject constructor(
    private val getAllTasksUseCase: GetAllYearlyTasksUseCase,
    private val addTaskUseCase: AddYearlyTaskUseCase,
    private val deleteTaskUseCase: DeleteYearlyTaskUseCase
) : BaseViewModel<YearlyTasksUiState, YearlyTasksEvents>(YearlyTasksUiState()),
    YearlyTasksInteractions {

    override fun initData() {
        _state.update { it.copy(contentStatus = ContentStatus.LOADING) }
        tryExecute(
            { getAllTasksUseCase() },
            ::allTasksSuccess,
            ::setFailureContent
        )
    }

    private fun allTasksSuccess(tasks: List<YearlyTaskEntity>) {
        _state.update { value ->
            value.copy(
                contentStatus = ContentStatus.VISIBLE,
                tasks = tasks.map { it.toUiState() }
            )
        }
    }

    private fun setFailureContent() {
        _state.update { it.copy(contentStatus = ContentStatus.FAILURE) }
    }


    override fun controlAddTaskVisibility() {
        _state.update { it.copy(isAddTaskVisible = !it.isAddTaskVisible) }
    }

    override fun onTitleChange(title: String) {
        _state.update { it.copy(addTask = it.addTask.copy(title = title)) }
    }

    override fun onDescriptionChange(description: String) {
        _state.update { it.copy(addTask = it.addTask.copy(description = description)) }
    }

    override fun onDateChange(date: String) {}

    override fun onTimeChange(time: String) {}

    override fun controlScheduleAlarmDialogVisibility() {}

    override fun controlEmptySchedulingDialogVisibility() {}

    override fun controlUnValidScheduledDialogVisibility() {}

    private fun validateAddTask(): Boolean {
        val value = state.value.addTask
        val validateTitle = value.title.validateRequireField()
        _state.update { it.copy(addTask = it.addTask.copy(titleError = !validateTitle)) }
        return validateTitle
    }

    override fun addTask() {
        if (validateAddTask()) {
            _state.update { it.copy(contentStatus = ContentStatus.LOADING) }
            tryExecute(
                { addTaskUseCase(state.value.addTask.toYearlyEntity()) },
                { addTaskSuccess() },
                ::setFailureContent
            )
        }
    }

    private fun addTaskSuccess() {
        _state.update {
            it.copy(
                contentStatus = ContentStatus.VISIBLE,
                tasks = it.tasks.toMutableList().apply {
                    add(it.addTask.toUiState().copy(id = generateRandomId()))
                },
                addTask = AddTaskUiState(),
                isAddTaskVisible = !it.isAddTaskVisible
            )
        }
    }

    override fun onSwipeDeleteTask(id: Int) {
        _state.update { it.copy(selectedDeleteItemId = id) }
    }

    override fun controlDeleteItemDialogVisibility() {
        _state.update { it.copy(isDeleteTaskDialogVisible = !it.isDeleteTaskDialogVisible) }
    }

    override fun deleteTask() {
        tryExecute(
            { deleteTaskUseCase(state.value.selectedDeleteItemId) },
            {
                _state.update { value ->
                    value.copy(
                        tasks = value.tasks.filterNot { it.id == state.value.selectedDeleteItemId }
                    )
                }
            },
            ::setFailureContent
        )
    }

}
