package com.example.dayflow.ui.daily_tasks.vm

import com.example.dayflow.data.local.entity.TaskEntity
import com.example.dayflow.data.usecase.GetAllTasksUseCase
import com.example.dayflow.ui.base.BaseViewModel
import com.example.dayflow.ui.utils.ContentStatus
import com.example.dayflow.ui.utils.ui_state.toUiState
import com.example.dayflow.ui.utils.validateRequireField
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class DailyTasksViewModel @Inject constructor(
    private val getAllTasksUseCase: GetAllTasksUseCase,
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
                tasks = tasks.map { it.toUiState() }
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

    override fun addTask() {
        if (validateAddTask()) {

        }
    }

    private fun validateAddTask(): Boolean {
        val validateTitle = state.value.addTask.title.validateRequireField()
        _state.update { it.copy(addTask = it.addTask.copy(titleError = !validateTitle)) }
        return validateTitle
    }

}
