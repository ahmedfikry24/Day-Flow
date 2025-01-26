package com.example.dayflow.utils

import com.example.dayflow.repository.FakeRepository
import io.mockk.clearAllMocks
import io.mockk.spyk
import org.junit.After
import org.junit.Before
import org.junit.Rule

abstract class BaseViewModelTester {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    protected lateinit var repository: FakeRepository
    protected lateinit var spyRepository: FakeRepository


    @Before
    open fun setUp() {
        repository = FakeRepository()
        spyRepository = spyk(repository)
    }


    @After
    open fun terminate() = clearAllMocks()
}