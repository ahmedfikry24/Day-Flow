package com.example.dayflow.ui.work_session

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.isNotDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.dayflow.MainActivity
import com.example.dayflow.R
import com.example.dayflow.ui.theme.DayFlowTheme
import com.example.dayflow.ui.utils.UiTestTags
import com.example.dayflow.utils.BaseAndroidTester
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test

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


    @Test
    fun pressArrowsUpAndDown_then_changeSessionDuration() {
        with(composeRule) {
            onNodeWithTag(UiTestTags.VISIBLE_CONTENT).isDisplayed()
            onNodeWithTag(UiTestTags.SESSION_INFO).isDisplayed()

            onNodeWithContentDescription(context.getString(R.string.plus_session_duration_icon)).performClick()
            onNodeWithText("35").assertIsDisplayed()

            onNodeWithContentDescription(context.getString(R.string.minus_session_duration_icon))
                .performClick()
                .performClick()

            onNodeWithText("25").assertIsDisplayed()
        }
    }

    @Test
    fun startSession_then_switchSessionContent() {
        with(composeRule) {
            onNodeWithTag(UiTestTags.VISIBLE_CONTENT).isDisplayed()
            onNodeWithTag(UiTestTags.SESSION_INFO).isDisplayed()

            onNodeWithText(context.getString(R.string.start_session)).performClick()

            onNodeWithTag(UiTestTags.SESSION_INFO).isNotDisplayed()
            onNodeWithTag(UiTestTags.SESSION_COUNT_DOWN).isDisplayed()
        }
    }

    @Test
    fun startSession_then_pauseItAndResumeAgain() {
        with(composeRule) {
            onNodeWithTag(UiTestTags.SESSION_INFO).isDisplayed()

            onNodeWithText(context.getString(R.string.start_session)).performClick()

            onNodeWithTag(UiTestTags.SESSION_INFO).isNotDisplayed()
            onNodeWithTag(UiTestTags.SESSION_COUNT_DOWN).isDisplayed()

            onNodeWithContentDescription(context.getString(R.string.pause_and_resume_icon))
                .performClick()
                .performClick()

            onNodeWithTag(UiTestTags.SESSION_COUNT_DOWN).isDisplayed()
        }
    }

    @Test
    fun startSession_then_finishItAndBackToInfo() {
        with(composeRule) {
            onNodeWithTag(UiTestTags.SESSION_INFO).isDisplayed()

            onNodeWithText(context.getString(R.string.start_session)).performClick()

            onNodeWithTag(UiTestTags.SESSION_INFO).isNotDisplayed()
            onNodeWithTag(UiTestTags.SESSION_COUNT_DOWN).isDisplayed()

            onNodeWithContentDescription(context.getString(R.string.finish_session_icon)).performClick()

            onNodeWithTag(UiTestTags.SESSION_COUNT_DOWN).isNotDisplayed()
            onNodeWithTag(UiTestTags.SESSION_INFO).isDisplayed()
        }
    }
}