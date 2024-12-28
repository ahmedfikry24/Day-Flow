package com.example.dayflow.ui.work_session.vm

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.example.dayflow.service.DefaultServiceManager
import com.example.dayflow.ui.base.BaseViewModel
import com.example.dayflow.ui.utils.convertSessionTimeToLong
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkSessionViewModel @Inject constructor(
    private val application: Application
) : BaseViewModel<WorkSessionUiState, WorkSessionEvents>(WorkSessionUiState()),
    WorkSessionInteractions {

    private var job: Job? = null

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
        controlSessionInfoVisibility()
        _state.update {
            it.copy(
                sessionDuration = it.sessionDurationMin.convertSessionTimeToLong(),
                sessionRemainingTime = it.sessionDurationMin.convertSessionTimeToLong(),
            )
        }
        resumeSession()
    }

    override fun resumeSession() {
        _state.update { it.copy(isRunning = true) }
        job = viewModelScope.launch {
            DefaultServiceManager.createSessionService(
                application,
                state.value.sessionRemainingTime
            )
            while (state.value.sessionRemainingTime > 0) {
                delay(1000L)
                _state.update { it.copy(sessionRemainingTime = it.sessionRemainingTime - 1000L) }
            }
            finishSession()
        }
    }

    override fun pauseSession() {
        job?.cancel()
        _state.update { it.copy(isRunning = false) }
        DefaultServiceManager.cancelSessionService(application)
    }

    override fun finishSession() {
        job?.cancel()
        _state.update {
            it.copy(
                isRunning = false,
                sessionDuration = 0,
                sessionRemainingTime = 0,
                isSessionInfoVisible = true
            )
        }
        DefaultServiceManager.cancelSessionService(application)
    }

    override fun onCleared() {
        job = null
        DefaultServiceManager.cancelSessionService(application)
        super.onCleared()
    }
}
