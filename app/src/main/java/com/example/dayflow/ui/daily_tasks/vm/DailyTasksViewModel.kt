package com.example.dayflow.ui.daily_tasks.vm

import com.example.dayflow.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DailyTasksViewModel @Inject constructor() :
    BaseViewModel<DailyTasksUiState, DailyTasksEvents>(DailyTasksUiState()),
    DailyTasksInteractions {

    override fun initData() {

    }

}
