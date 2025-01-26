package com.example.dayflow.daily_tasks

import com.example.dayflow.data.alarm.DefaultAlarmManager
import com.example.dayflow.data.usecase.AddDailyTaskUseCase
import com.example.dayflow.data.usecase.DeleteDailyTaskUseCase
import com.example.dayflow.data.usecase.GetAllDailyTasksUseCase
import com.example.dayflow.data.usecase.UpdateDailyTaskStatusUseCase
import com.example.dayflow.repository.FakeRepository
import com.example.dayflow.ui.daily_tasks.vm.DailyTasksViewModel
import com.example.dayflow.utils.MainDispatcherRule
import io.mockk.mockk
import io.mockk.spyk
import org.junit.Before
import org.junit.Rule


class DailyTaskViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var repository: FakeRepository
    private lateinit var spyRepository: FakeRepository
    private lateinit var getAllDailyTasksUseCase: GetAllDailyTasksUseCase
    private lateinit var defaultAlarmManager: DefaultAlarmManager
    private lateinit var addDailyTasksUseCase: AddDailyTaskUseCase
    private lateinit var updateDailyTaskStatusUseCase: UpdateDailyTaskStatusUseCase
    private lateinit var deleteDailyTaskUseCase: DeleteDailyTaskUseCase
    private lateinit var viewModel: DailyTasksViewModel

    @Before
    fun setUp() {
        repository = FakeRepository()
        spyRepository = spyk(repository)
        getAllDailyTasksUseCase = GetAllDailyTasksUseCase(spyRepository)
        defaultAlarmManager = mockk()
        addDailyTasksUseCase = AddDailyTaskUseCase(spyRepository, defaultAlarmManager)
        updateDailyTaskStatusUseCase =
            UpdateDailyTaskStatusUseCase(spyRepository, defaultAlarmManager)
        deleteDailyTaskUseCase = DeleteDailyTaskUseCase(spyRepository, defaultAlarmManager)
        viewModel = DailyTasksViewModel(
            getAllDailyTasksUseCase,
            addDailyTasksUseCase,
            updateDailyTaskStatusUseCase,
            deleteDailyTaskUseCase
        )
    }


}