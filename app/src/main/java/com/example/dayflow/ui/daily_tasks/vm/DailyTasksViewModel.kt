package com.example.dayflow.ui.daily_tasks.vm

import com.example.dayflow.data.local.entity.DailyTaskEntity
import com.example.dayflow.data.usecase.AddDailyTaskUseCase
import com.example.dayflow.data.usecase.DeleteDailyTaskUseCase
import com.example.dayflow.data.usecase.GetAllDailyTasksUseCase
import com.example.dayflow.data.usecase.UpdateDailyTaskStatusUseCase
import com.example.dayflow.ui.base.BaseViewModel
import com.example.dayflow.ui.utils.ContentStatus
import com.example.dayflow.ui.utils.UiConstants
import com.example.dayflow.ui.utils.getAlarmTime
import com.example.dayflow.ui.utils.ui_state.AddTaskUiState
import com.example.dayflow.ui.utils.ui_state.TaskUiState
import com.example.dayflow.ui.utils.ui_state.toDailyEntity
import com.example.dayflow.ui.utils.ui_state.toUiState
import com.example.dayflow.ui.utils.validateRequireField
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class DailyTasksViewModel @Inject constructor(
    private val getAllTasksUseCase: GetAllDailyTasksUseCase,
    private val addDailyTaskUseCase: AddDailyTaskUseCase,
    private val updateTaskStatusUseCase: UpdateDailyTaskStatusUseCase,
    private val deleteDailyTaskUseCase: DeleteDailyTaskUseCase,
) : BaseViewModel<DailyTasksUiState, DailyTasksEvents>(DailyTasksUiState()),
    DailyTasksInteractions {

    override fun initData() {
        tryExecute(
            { getAllTasksUseCase() },
            ::allTasksSuccess,
            ::setFailureContent
        )
    }

    private fun allTasksSuccess(tasks: List<DailyTaskEntity>) {
        updateAddTaskId(tasks)
        _state.update { value ->
            value.copy(
                contentStatus = ContentStatus.VISIBLE,
                doneTasks = tasks.filter { it.status }.map { it.toUiState() },
                inProgressTasks = tasks.filter { !it.status }.map { it.toUiState() },
            )
        }
    }

    private fun setFailureContent() {
        _state.update { it.copy(contentStatus = ContentStatus.FAILURE) }
    }

    private fun updateAddTaskId(tasks: List<DailyTaskEntity>) {
        if (tasks.isEmpty()) return
        tasks.maxBy { it.id }.also { UiConstants.lastDailyTaskId = it.id + 1 }
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

    override fun controlUnValidScheduledDialogVisibility() {
        _state.update { it.copy(addTask = it.addTask.copy(isScheduledUnValid = !it.addTask.isScheduledUnValid)) }
    }

    private fun validateAddTask(): Boolean {
        val value = state.value.addTask
        val validateTitle = value.title.validateRequireField()
        val validateDate =
            value.date != UiConstants.INITIAL_DATE && value.time == UiConstants.INITIAL_TIME
        val validateTime =
            value.time != UiConstants.INITIAL_TIME && value.date == UiConstants.INITIAL_DATE
        var isSelectedDateValid = true
        val isHasError = mutableListOf(
            validateTitle,
            !validateDate,
            !validateTime,
        ).any { !it }

        if (value.date != UiConstants.INITIAL_DATE && value.time != UiConstants.INITIAL_TIME)
            isSelectedDateValid = getAlarmTime(value.date, value.time) > System.currentTimeMillis()

        _state.update {
            it.copy(
                addTask = it.addTask.copy(
                    titleError = !validateTitle,
                    isSchedulingEmptyDialogVisibility = validateDate || validateTime,
                    isScheduledUnValid = !isSelectedDateValid
                )
            )
        }
        return !isHasError
    }

    override fun addTask() {
        if (validateAddTask()) {
            _state.update { it.copy(contentStatus = ContentStatus.LOADING) }
            tryExecute(
                { addDailyTaskUseCase(state.value.addTask.toDailyEntity()) },
                { addTaskSuccess() },
                ::setFailureContent
            )
        }
    }

    private fun addTaskSuccess() {
        _state.update {
            it.copy(
                contentStatus = ContentStatus.VISIBLE,
                inProgressTasks = it.inProgressTasks.toMutableList().apply {
                    add(it.addTask.toUiState(UiConstants.lastDailyTaskId))
                },
                addTask = AddTaskUiState(),
                isAddTaskVisible = !it.isAddTaskVisible
            )
        }
        UiConstants.lastDailyTaskId++
    }

    override fun onSwipeDoneTask(task: TaskUiState) {
        tryExecute(
            { updateTaskStatusUseCase(task.id) },
            {
                _state.update { value ->
                    value.copy(
                        inProgressTasks = value.inProgressTasks.filterNot { it.id == task.id },
                        doneTasks = value.doneTasks.toMutableList()
                            .apply { add(task.copy(isDone = true)) }
                    )
                }
            },
            ::setFailureContent
        )
    }

    override fun onSwipeDeleteTask(task: TaskUiState) {
        _state.update { it.copy(selectedDeleteItem = task) }
    }

    override fun controlDeleteItemDialogVisibility() {
        _state.update { it.copy(isDeleteTaskDialogVisible = !it.isDeleteTaskDialogVisible) }
    }

    override fun deleteTask() {
        tryExecute(
            { deleteDailyTaskUseCase(state.value.selectedDeleteItem.toDailyEntity()) },
            {
                _state.update { value ->
                    value.copy(
                        inProgressTasks = value.inProgressTasks.filterNot { it.id == state.value.selectedDeleteItem.id }
                    )
                }
            },
            ::setFailureContent
        )
    }
}
