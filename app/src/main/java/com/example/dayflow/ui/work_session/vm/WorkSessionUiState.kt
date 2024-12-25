package com.example.dayflow.ui.work_session.vm

import com.example.dayflow.ui.utils.ContentStatus

data class WorkSessionUiState(
    val contentStatus: ContentStatus = ContentStatus.VISIBLE,
    val isSessionInfoVisible: Boolean = true,
    val sessionDurationMin: Int = 30,
    val sessionId: Int = 0,
    val sessionsCount: Int = 0,
    val sessionDuration: Long = 0L,
    val isRunning: Boolean = false,
    val sessionRemainingTime: Long = 0L
) 
