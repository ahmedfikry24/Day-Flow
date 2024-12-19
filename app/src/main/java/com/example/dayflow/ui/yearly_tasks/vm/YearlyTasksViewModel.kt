package com.example.dayflow.ui.yearly_tasks.vm

import com.example.dayflow.data.local.entity.YearlyTaskEntity
import com.example.dayflow.data.usecase.GetAllYearlyTasksUseCase
import com.example.dayflow.ui.base.BaseViewModel
import com.example.dayflow.ui.utils.ContentStatus
import com.example.dayflow.ui.utils.ui_state.toUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class YearlyTasksViewModel @Inject constructor(
    private val getAllTasksUseCase: GetAllYearlyTasksUseCase
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


}
