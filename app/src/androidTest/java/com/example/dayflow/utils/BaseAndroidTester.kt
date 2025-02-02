package com.example.dayflow.utils

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.dayflow.data.local.LocalDataBase
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
abstract class BaseAndroidTester {

    @get:Rule
    val mainDispatcher = MainDispatcherRule()

    protected lateinit var context: Context
    protected lateinit var roomDatabase: LocalDataBase

    @Before
    open fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        roomDatabase = Room.inMemoryDatabaseBuilder(context, LocalDataBase::class.java).build()
    }

    @After
    open fun terminate() {
        roomDatabase.close()
    }
}