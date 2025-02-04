package com.example.dayflow.ui.daily_tasks

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.navigation.compose.rememberNavController
import androidx.test.rule.GrantPermissionRule
import com.example.dayflow.MainActivity
import com.example.dayflow.ui.utils.UiTestTags
import com.example.dayflow.utils.BaseAndroidTester
import com.example.dayflow.utils.denyNotificationPermission
import com.example.dayflow.utils.grantNotificationPermission
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
}