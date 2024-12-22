package com.example.dayflow.ui.work_session.vm

import com.example.dayflow.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WorkSessionViewModel @Inject constructor(

) : BaseViewModel<WorkSessionUiState, WorkSessionEvents>(WorkSessionUiState()),
    WorkSessionInteractions {

    override fun initData() {

    }

}
