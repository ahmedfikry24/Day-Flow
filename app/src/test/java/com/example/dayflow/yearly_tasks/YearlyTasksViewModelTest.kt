package com.example.dayflow.yearly_tasks

import com.example.dayflow.data.usecase.AddYearlyTaskUseCase
import com.example.dayflow.data.usecase.DeleteYearlyTaskUseCase
import com.example.dayflow.data.usecase.GetAllYearlyTasksUseCase
import com.example.dayflow.ui.yearly_tasks.vm.YearlyTasksViewModel
import com.example.dayflow.utils.BaseViewModelTester

class YearlyTasksViewModelTest : BaseViewModelTester() {

    private lateinit var getAllYearlyTasksUseCase: GetAllYearlyTasksUseCase
    private lateinit var addYearlyTasksUseCase: AddYearlyTaskUseCase
    private lateinit var deleteYearlyTaskUseCase: DeleteYearlyTaskUseCase
    private lateinit var viewModel: YearlyTasksViewModel

    override fun setUp() {
        super.setUp()
        getAllYearlyTasksUseCase = GetAllYearlyTasksUseCase(spyRepository)
        addYearlyTasksUseCase = AddYearlyTaskUseCase(spyRepository)
        deleteYearlyTaskUseCase = DeleteYearlyTaskUseCase(spyRepository)
        viewModel = YearlyTasksViewModel(
            getAllYearlyTasksUseCase,
            addYearlyTasksUseCase,
            deleteYearlyTaskUseCase
        )
    }

}