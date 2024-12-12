package com.example.dayflow.ui.daily_tasks.vm

import com.example.dayflow.data.local.entity.TaskEntity
import com.example.dayflow.data.usecase.AddDailyTaskUseCase
import com.example.dayflow.data.usecase.GetAllTasksUseCase
import com.example.dayflow.ui.base.BaseViewModel
import com.example.dayflow.ui.utils.ContentStatus
import com.example.dayflow.ui.utils.ui_state.AddTaskUiState
import com.example.dayflow.ui.utils.ui_state.INITIAL_DATE
import com.example.dayflow.ui.utils.ui_state.INITIAL_TIME
import com.example.dayflow.ui.utils.ui_state.toEntity
import com.example.dayflow.ui.utils.ui_state.toUiState
import com.example.dayflow.ui.utils.validateRequireField
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class DailyTasksViewModel @Inject constructor(
    private val getAllTasksUseCase: GetAllTasksUseCase,
    private val addDailyTaskUseCase: AddDailyTaskUseCase,
) : BaseViewModel<DailyTasksUiState, DailyTasksEvents>(DailyTasksUiState()),
    DailyTasksInteractions {

    override fun initData() {
        tryExecute(
            { getAllTasksUseCase() },
            ::allTasksSuccess,
            ::allTasksError
        )
    }

    private fun allTasksSuccess(tasks: List<TaskEntity>) {
        _state.update { value ->
            value.copy(
                contentStatus = ContentStatus.VISIBLE,
                doneTasks = tasks.filter { it.status }.map { it.toUiState() },
                inProgressTasks = tasks.filter { !it.status }.map { it.toUiState() },
            )
        }
    }

    private fun allTasksError() {
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

    override fun onDateChange(date: String) {
        _state.update { it.copy(addTask = it.addTask.copy(date = date)) }
    }

    override fun onTimeChange(time: String) {
        _state.update { it.copy(addTask = it.addTask.copy(time = time)) }
    }

    override fun controlScheduleAlarmDialogVisibility() {
        _state.update { it.copy(addTask = it.addTask.copy(canScheduleAlarmDialogVisibility = !it.addTask.canScheduleAlarmDialogVisibility)) }
    }

    override fun controlEmptySchedulingDialogVisibility() {
        _state.update { it.copy(addTask = it.addTask.copy(isSchedulingEmptyDialogVisibility = !it.addTask.isSchedulingEmptyDialogVisibility)) }
    }

    private fun validateAddTask(): Boolean {
        val value = state.value.addTask
        val validateTitle = value.title.validateRequireField()
        val validateDate = value.date != INITIAL_DATE && value.time == INITIAL_TIME
        val validateTime = value.time != INITIAL_TIME && value.date == INITIAL_DATE

        val isHasError = listOf(validateTitle, !validateDate, !validateTime).any { !it }

        _state.update {
            it.copy(
                addTask = it.addTask.copy(
                    titleError = !validateTitle,
                    isSchedulingEmptyDialogVisibility = validateDate || validateTime
                )
            )
        }
        return !isHasError
    }

    override fun addTask() {
        if (validateAddTask()) {
            _state.update { it.copy(contentStatus = ContentStatus.LOADING) }
            tryExecute(
                { addDailyTaskUseCase(state.value.addTask.toEntity()) },
                { addTaskSuccess() },
                ::addTaskError
            )
        }
    }

    private fun generateRandomId(): Int {
        return (Int.MIN_VALUE..Int.MAX_VALUE).random()
    }

    private fun addTaskSuccess() {
        _state.update {
            it.copy(
                contentStatus = ContentStatus.VISIBLE,
                inProgressTasks = it.inProgressTasks.toMutableList().apply {
                    add(it.addTask.toUiState().copy(id = generateRandomId()))
                },
                addTask = AddTaskUiState(),
                isAddTaskVisible = !it.isAddTaskVisible
            )
        }
    }

    private fun addTaskError() {
        _state.update { it.copy(contentStatus = ContentStatus.FAILURE) }
    }
}
