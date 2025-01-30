package com.example.dayflow.ui.work_session.vm

interface WorkSessionInteractions {
    fun initData()
    fun controlSessionInfoVisibility()
    fun plusSessionDurationMin()
    fun minusSessionDurationMin()
    fun startSession()
    fun onChangeSessionRemainingTime()
    fun pauseSession()
    fun resumeSession()
    fun finishSession()
}
