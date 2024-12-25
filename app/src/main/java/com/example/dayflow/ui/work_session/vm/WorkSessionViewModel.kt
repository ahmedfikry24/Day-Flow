package com.example.dayflow.ui.work_session.vm

import com.example.dayflow.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class WorkSessionViewModel @Inject constructor(

) : BaseViewModel<WorkSessionUiState, WorkSessionEvents>(WorkSessionUiState()),
    WorkSessionInteractions {

    override fun initData() {}

    override fun controlSessionInfoVisibility() {
        _state.update { it.copy(isSessionInfoVisible = !it.isSessionInfoVisible) }
    }

    override fun plusSessionDurationMin() {
        _state.update { it.copy(sessionDurationMin = it.sessionDurationMin + 5) }
    }

    override fun minusSessionDurationMin() {
        _state.update { it.copy(sessionDurationMin = it.sessionDurationMin - 5) }
    }

    override fun startSession() {

    }

}
