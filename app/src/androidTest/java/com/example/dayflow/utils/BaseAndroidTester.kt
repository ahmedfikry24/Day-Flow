package com.example.dayflow.utils

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.dayflow.data.local.LocalDataBase
import com.example.dayflow.data.repository.FakeRepositoryImpl
import dagger.hilt.android.testing.HiltAndroidRule
import io.mockk.spyk
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
abstract class BaseAndroidTester {

    @get:Rule
    val mainDispatcher = MainDispatcherRule()

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    protected lateinit var context: Context

    @Inject
    lateinit var roomDatabase: LocalDataBase

    @Inject
    lateinit var repository: FakeRepositoryImpl
    protected lateinit var spyRepository: FakeRepositoryImpl

    @Before
    open fun setUp() {
        hiltRule.inject()
        context = ApplicationProvider.getApplicationContext()
        spyRepository = spyk(repository)
    }

    @After
    open fun terminate() {
        roomDatabase.close()
    }
}