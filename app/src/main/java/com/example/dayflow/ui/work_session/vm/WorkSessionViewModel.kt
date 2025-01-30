package com.example.dayflow.ui.work_session.vm

import android.app.Application
import com.example.dayflow.service.DefaultServiceManager
import com.example.dayflow.service.isBlockNotificationListenerActive
import com.example.dayflow.ui.base.BaseViewModel
import com.example.dayflow.ui.utils.convertSessionTimeToLong
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class WorkSessionViewModel @Inject constructor(
    private val application: Application
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
        _state.update {
            it.copy(
                isSessionInfoVisible = false,
                sessionDuration = it.sessionDurationMin.convertSessionTimeToLong(),
                sessionRemainingTime = it.sessionDurationMin.convertSessionTimeToLong(),
                isRunning = true
            )
        }
        DefaultServiceManager.createSessionService(
            application,
            state.value.sessionRemainingTime
        )
        isBlockNotificationListenerActive = true
    }

    override fun onChangeSessionRemainingTime() {
        _state.update { it.copy(sessionRemainingTime = it.sessionRemainingTime - 1000L) }
    }

    override fun resumeSession() {
        _state.update { it.copy(isRunning = true) }
        DefaultServiceManager.createSessionService(
            application,
            state.value.sessionRemainingTime
        )
    }

    override fun pauseSession() {
        _state.update { it.copy(isRunning = false) }
        DefaultServiceManager.cancelSessionService(application)
        isBlockNotificationListenerActive = false
    }

    override fun finishSession() {
        _state.update {
            it.copy(
                isRunning = false,
                sessionDuration = 0,
                sessionRemainingTime = 0,
                isSessionInfoVisible = true
            )
        }
        DefaultServiceManager.cancelSessionService(application)
        isBlockNotificationListenerActive = false
    }

    override fun onCleared() {
        DefaultServiceManager.cancelSessionService(application)
        isBlockNotificationListenerActive = false
        super.onCleared()
    }
}
