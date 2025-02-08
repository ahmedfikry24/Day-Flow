package com.example.dayflow.ui.work_session

import androidx.activity.compose.setContent
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.example.dayflow.MainActivity
import com.example.dayflow.ui.theme.DayFlowTheme
import com.example.dayflow.utils.BaseAndroidTester
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule

@HiltAndroidTest
class WorkSessionScreenTest : BaseAndroidTester() {

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()


    override fun setUp() {
        super.setUp()
        composeRule.activity.setContent {
            DayFlowTheme {
                WorkSessionScreen()
            }
        }
    }

}