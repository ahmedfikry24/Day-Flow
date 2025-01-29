package com.example.dayflow.work_session

import com.example.dayflow.service.DefaultServiceManager
import com.example.dayflow.ui.work_session.vm.WorkSessionViewModel
import com.example.dayflow.utils.BaseViewModelTester
import io.mockk.mockk

class WorkSessionViewModelTest : BaseViewModelTester() {

    private lateinit var serviceManager: DefaultServiceManager
    private lateinit var viewModel: WorkSessionViewModel

    override fun setUp() {
        super.setUp()
        serviceManager = mockk()
        viewModel = WorkSessionViewModel(serviceManager)
    }


}