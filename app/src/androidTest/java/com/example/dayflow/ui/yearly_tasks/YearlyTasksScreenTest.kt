package com.example.dayflow.ui.yearly_tasks

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeLeft
import androidx.navigation.compose.rememberNavController
import com.example.dayflow.MainActivity
import com.example.dayflow.R
import com.example.dayflow.ui.utils.UiTestTags
import com.example.dayflow.utils.BaseAndroidTester
import com.example.dayflow.utils.grantNotificationPermission
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class YearlyTasksScreenTest : BaseAndroidTester() {

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()


    override fun setUp() {
        super.setUp()
        composeRule.activity.setContent {
            YearlyTasksScreen(rememberNavController())
        }
    }

    @Test
    fun pressAddTaskFAB_then_transitAddTaskContent() {
        grantNotificationPermission()
        composeRule.onNodeWithTag(UiTestTags.VISIBLE_CONTENT).assertIsDisplayed()

        composeRule.onNodeWithTag(UiTestTags.ADD_TASK_FAB).performClick()
        composeRule.onNodeWithTag(UiTestTags.ADD_TASK_CONTENT).assertIsDisplayed()

        composeRule.onNodeWithText(context.getString(R.string.cancel)).performClick()
        composeRule.onNodeWithTag(UiTestTags.ADD_TASK_FAB).assertIsDisplayed()
        composeRule.onNodeWithTag(UiTestTags.ADD_TASK_CONTENT).assertIsNotDisplayed()
    }

    @Test
    fun addTask_then_TaskAddedSuccess() {
        composeRule.onNodeWithTag(UiTestTags.VISIBLE_CONTENT).assertIsDisplayed()

        composeRule.onNodeWithTag(UiTestTags.ADD_TASK_FAB).performClick()
        composeRule.onNodeWithTag(UiTestTags.ADD_TASK_CONTENT).assertIsDisplayed()

        composeRule.onNodeWithText(context.getString(R.string.headline)).performTextInput("title")
        composeRule.onNodeWithText(context.getString(R.string.description))
            .performTextInput("description")
        composeRule.onNodeWithText(context.getString(R.string.add_task)).performClick()

        composeRule.onNodeWithTag(UiTestTags.ADD_TASK_CONTENT).assertIsNotDisplayed()
        composeRule.onNodeWithText("title").assertIsDisplayed()
    }

    @Test
    fun swipeToDeleteTask_then_deleteTaskSuccess() {
        addTask_then_TaskAddedSuccess()
        val taskTitle = "title"
        with(composeRule) {
            waitForIdle()

            onNodeWithText(taskTitle).assertIsDisplayed()
            onNodeWithTag(UiTestTags.TASK_CONTENT).performTouchInput {
                swipeLeft()
            }
            onNodeWithText(context.getString(R.string.delete)).performClick()
            onNodeWithText(taskTitle).assertIsNotDisplayed()
        }
    }
}