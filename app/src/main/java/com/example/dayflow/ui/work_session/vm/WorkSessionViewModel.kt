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
        DefaultServiceManager.createSessionService(application, state.value.sessionDurationMin)
        controlSessionRunning()
    }


    override fun controlSessionRunning() {
        _state.update { it.copy(isRunning = !it.isRunning) }
        if (state.value.isRunning) {
            job = viewModelScope.launch {
                while (state.value.sessionRemainingTime > 0) {
                    delay(1000L)
                    _state.update { it.copy(sessionRemainingTime = it.sessionRemainingTime - 1000L) }
                }
                finishSession()
            }
        } else job?.cancel()
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
