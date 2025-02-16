package com.example.dayflow.ui.settings

import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.testing.TestNavHostController
import com.example.dayflow.MainActivity
import com.example.dayflow.navigation.AppDestination
import com.example.dayflow.ui.block_apps_notification.BlockAppsNotificationScreen
import com.example.dayflow.ui.theme.DayFlowTheme
import com.example.dayflow.utils.BaseAndroidTester
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule

@HiltAndroidTest
class SettingsScreenTest : BaseAndroidTester() {

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()
    private lateinit var navController: TestNavHostController


    override fun setUp() {
        super.setUp()
        composeRule.activity.setContent {
            DayFlowTheme {
                navController = TestNavHostController(LocalContext.current)
                navController.navigatorProvider.addNavigator(ComposeNavigator())

                NavHost(navController, startDestination = AppDestination.Settings) {
                    composable<AppDestination.Settings> { SettingsScreen(navController) }
                    composable<AppDestination.BlockAppsNotification> {
                        BlockAppsNotificationScreen(
                            navController
                        )
                    }
                }

            }
        }
    }
}