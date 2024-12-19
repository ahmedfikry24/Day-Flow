package com.example.dayflow.ui.yearly_tasks.vm

import com.example.dayflow.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class YearlyTasksViewModel @Inject constructor() :
    BaseViewModel<YearlyTasksUiState, YearlyTasksEvents>(YearlyTasksUiState()),
    YearlyTasksInteractions {

    override fun initData() {

    }

}
