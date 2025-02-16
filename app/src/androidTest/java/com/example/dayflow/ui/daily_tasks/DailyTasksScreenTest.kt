package com.example.dayflow.ui.daily_tasks

import android.os.Build
import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeLeft
import androidx.compose.ui.test.swipeRight
import androidx.navigation.compose.rememberNavController
import androidx.test.rule.GrantPermissionRule
import com.example.dayflow.MainActivity
import com.example.dayflow.R
import com.example.dayflow.ui.utils.UiConstants
import com.example.dayflow.ui.utils.UiTestTags
import com.example.dayflow.ui.utils.checkScheduleAlarmPermission
import com.example.dayflow.utils.BaseAndroidTester
import com.example.dayflow.utils.denyNotificationPermission
import com.example.dayflow.utils.grantNotificationPermission
import com.example.dayflow.utils.grantScheduleAlarmPermission
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test


@HiltAndroidTest
class DailyTasksScreenTest : BaseAndroidTester() {

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @get:Rule(order = 2)
    val permissionRule: GrantPermissionRule =
        GrantPermissionRule.grant(android.Manifest.permission.POST_NOTIFICATIONS)

    override fun setUp() {
        super.setUp()
        composeRule.activity.setContent {
            DailyTasksScreen(rememberNavController())
        }
    }

    @Test
    fun initScreen_then_grantNotificationPermission() {

        grantNotificationPermission()

        composeRule.onNodeWithTag(UiTestTags.VISIBLE_CONTENT).assertIsDisplayed()
    }


    @Test
    fun initScreen_then_denyNotificationPermission() {

        denyNotificationPermission()

        composeRule.onNodeWithTag(UiTestTags.VISIBLE_CONTENT).assertIsDisplayed()
    }

    @Test
    fun pressTabs_then_toggleBetweenContent() {
        grantNotificationPermission()
        composeRule.onNodeWithTag(UiTestTags.VISIBLE_CONTENT).assertIsDisplayed()

        composeRule.onNodeWithText("In Progress").performClick()

        composeRule.onNodeWithTag(UiTestTags.IN_PROGRESS_DAILY_TASKS_CONTENT).assertIsDisplayed()
        composeRule.onNodeWithTag(UiTestTags.DONE_DAILY_TASKS_CONTENT).assertIsNotDisplayed()

        composeRule.onNodeWithText("Done").performClick()

        composeRule.onNodeWithTag(UiTestTags.DONE_DAILY_TASKS_CONTENT).assertIsDisplayed()
        composeRule.onNodeWithTag(UiTestTags.IN_PROGRESS_DAILY_TASKS_CONTENT).assertIsNotDisplayed()
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
    fun insertTaskWithoutScheduling_then_TaskAddedSuccess() {
        grantNotificationPermission()
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
    fun insertTaskWithScheduling_then_grantScheduleAlarmPermission() {
        grantNotificationPermission()

        with(composeRule) {
            waitForIdle()
            onNodeWithTag(UiTestTags.VISIBLE_CONTENT).assertIsDisplayed()

            onNodeWithTag(UiTestTags.ADD_TASK_FAB).performClick()
            onNodeWithText(context.getString(R.string.headline)).performTextInput("title")
            onNodeWithText(context.getString(R.string.description)).performTextInput("description")
            onNodeWithText(UiConstants.INITIAL_DATE).performClick()
            onNodeWithText(context.getString(R.string.ok)).performClick()
            waitForIdle()

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !context.checkScheduleAlarmPermission())
                grantScheduleAlarmPermission()

            onNodeWithTag(UiTestTags.ADD_TASK_CONTENT).isDisplayed()
        }
    }


    @Test
    fun addTaskWithTimeOnly_then_showEmptyScheduleDialog() {
        if (!context.checkScheduleAlarmPermission()) {
            insertTaskWithScheduling_then_grantScheduleAlarmPermission()

            with(composeRule) {
                waitForIdle()

                onNodeWithText(UiConstants.INITIAL_TIME).performClick()
                onNodeWithTag(UiTestTags.TIME_PICKER_MODAL).assertIsDisplayed()
                onNodeWithText(context.getString(R.string.ok)).performClick()
                waitForIdle()

                onNodeWithText(context.getString(R.string.add_task)).performClick()
                waitForIdle()

                onNodeWithText(context.getString(R.string.not_complete)).assertIsDisplayed()
            }

        } else {
            grantNotificationPermission()

            with(composeRule) {
                waitForIdle()
                onNodeWithTag(UiTestTags.VISIBLE_CONTENT).assertIsDisplayed()

                onNodeWithTag(UiTestTags.ADD_TASK_FAB).performClick()
                onNodeWithTag(UiTestTags.ADD_TASK_CONTENT).isDisplayed()
                onNodeWithText(context.getString(R.string.headline)).performTextInput("title")
                onNodeWithText(context.getString(R.string.description)).performTextInput("description")
                waitForIdle()

                onNodeWithText(UiConstants.INITIAL_TIME).performClick()
                onNodeWithTag(UiTestTags.TIME_PICKER_MODAL).assertIsDisplayed()
                onNodeWithText(context.getString(R.string.ok)).performClick()
                waitForIdle()

                onNodeWithText(context.getString(R.string.add_task)).performClick()
                waitForIdle()

                onNodeWithText(context.getString(R.string.not_complete)).assertIsDisplayed()
            }
        }
    }

    @Test
    fun addTaskWithDateOnly_then_showEmptyScheduleDialog() {
        if (!context.checkScheduleAlarmPermission()) {
            insertTaskWithScheduling_then_grantScheduleAlarmPermission()

            with(composeRule) {
                waitForIdle()

                onNodeWithText(UiConstants.INITIAL_DATE).performClick()
                onNodeWithTag(UiTestTags.DATE_PICKER_MODAL).assertIsDisplayed()
                onNodeWithText(context.getString(R.string.ok)).performClick()
                waitForIdle()

                onNodeWithText(context.getString(R.string.add_task)).performClick()
                waitForIdle()

                onNodeWithText(context.getString(R.string.not_complete)).assertIsDisplayed()
            }

        } else {
            grantNotificationPermission()

            with(composeRule) {
                waitForIdle()
                onNodeWithTag(UiTestTags.VISIBLE_CONTENT).assertIsDisplayed()

                onNodeWithTag(UiTestTags.ADD_TASK_FAB).performClick()
                onNodeWithTag(UiTestTags.ADD_TASK_CONTENT).isDisplayed()
                onNodeWithText(context.getString(R.string.headline)).performTextInput("title")
                onNodeWithText(context.getString(R.string.description)).performTextInput("description")
                waitForIdle()

                onNodeWithText(UiConstants.INITIAL_DATE).performClick()
                onNodeWithTag(UiTestTags.DATE_PICKER_MODAL).assertIsDisplayed()
                onNodeWithText(context.getString(R.string.ok)).performClick()
                waitForIdle()

                onNodeWithText(context.getString(R.string.add_task)).performClick()
                waitForIdle()

                onNodeWithText(context.getString(R.string.not_complete)).assertIsDisplayed()
            }
        }
    }


    @Test
    fun addTaskWithFullSchedule_then_taskAddedSuccess() {
        if (!context.checkScheduleAlarmPermission()) {
            insertTaskWithScheduling_then_grantScheduleAlarmPermission()
            with(composeRule) {
                waitForIdle()

                onNodeWithText(UiConstants.INITIAL_DATE).performClick()
                onNodeWithTag(UiTestTags.DATE_PICKER_MODAL).assertIsDisplayed()
                onNodeWithText(context.getString(R.string.ok)).performClick()
                waitForIdle()

                onNodeWithText(UiConstants.INITIAL_TIME).performClick()
                onNodeWithTag(UiTestTags.TIME_PICKER_MODAL).assertIsDisplayed()
                onNodeWithText(context.getString(R.string.ok)).performClick()
                waitForIdle()

                onNodeWithText(context.getString(R.string.add_task)).performClick()
                waitForIdle()
            }
        } else {
            grantNotificationPermission()

            with(composeRule) {
                waitForIdle()

                onNodeWithText(UiConstants.INITIAL_DATE).performClick()
                onNodeWithTag(UiTestTags.DATE_PICKER_MODAL).assertIsDisplayed()
                onNodeWithText(context.getString(R.string.ok)).performClick()
                waitForIdle()

                onNodeWithText(UiConstants.INITIAL_TIME).performClick()
                onNodeWithTag(UiTestTags.TIME_PICKER_MODAL).assertIsDisplayed()
                onNodeWithText(context.getString(R.string.ok)).performClick()
                waitForIdle()

                onNodeWithText(context.getString(R.string.add_task)).performClick()
                waitForIdle()
            }
        }

    }

    @Test
    fun swipeToDeleteTask_then_deleteTaskSuccess() {
        insertTaskWithoutScheduling_then_TaskAddedSuccess()
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

    @Test
    fun swipeToDoneTask_then_addedTaskInDoneSuccess() {
        insertTaskWithoutScheduling_then_TaskAddedSuccess()
        val taskTitle = "title"
        with(composeRule) {
            waitForIdle()

            onNodeWithText(taskTitle).assertIsDisplayed()
            onNodeWithTag(UiTestTags.TASK_CONTENT).performTouchInput {
                swipeRight()
            }
            onNodeWithText(taskTitle).assertIsNotDisplayed()
            waitForIdle()

            composeRule.onNodeWithText(context.getString(R.string.done)).performClick()
            onNodeWithText(taskTitle).assertIsDisplayed()
        }
    }
}