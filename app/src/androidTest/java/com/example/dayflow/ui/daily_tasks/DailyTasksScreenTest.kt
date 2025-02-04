package com.example.dayflow.ui.daily_tasks

import androidx.activity.compose.setContent
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.compose.rememberNavController
import com.example.dayflow.MainActivity
import com.example.dayflow.utils.BaseAndroidTester
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule


@HiltAndroidTest
class DailyTasksScreenTest : BaseAndroidTester() {

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    override fun setUp() {
        super.setUp()
        composeRule.activity.setContent {
            DailyTasksScreen(rememberNavController())
        }
    }
}