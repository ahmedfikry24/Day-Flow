package com.example.dayflow.ui.settings

import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.testing.TestNavHostController
import com.example.dayflow.MainActivity
import com.example.dayflow.navigation.AppDestination
import com.example.dayflow.ui.block_apps_notification.BlockAppsNotificationScreen
import com.example.dayflow.ui.theme.DayFlowTheme
import com.example.dayflow.ui.utils.UiTestTags
import com.example.dayflow.utils.BaseAndroidTester
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class SettingsScreenTest : BaseAndroidTester() {

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()
    private lateinit var navController: TestNavHostController
    private var isDarkTheme = false

    override fun setUp() {
        super.setUp()
        composeRule.activity.setContent {
            navController = TestNavHostController(LocalContext.current)
            navController.navigatorProvider.addNavigator(ComposeNavigator())
            DayFlowTheme(darkTheme = isDarkTheme) {
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


    @Test
    fun clickDarkTheme_then_toggleBetweenThemes() {
        with(composeRule) {
            onNodeWithTag(UiTestTags.SWITCH_THEME_BUTTON).performClick()
            isDarkTheme = !isDarkTheme
            assertTrue(isDarkTheme)
            waitForIdle()

            onNodeWithTag(UiTestTags.SWITCH_THEME_BUTTON).performClick()
            isDarkTheme = !isDarkTheme
            assertFalse(isDarkTheme)
        }
    }


    @Test
    fun clickBlockApps_then_navigateToBlockAppsScreen() {
        with(composeRule) {
            onNodeWithTag(UiTestTags.BLOCK_APPS_NOTIFICATION_BUTTON).performClick()
            navController.assertCurrentRouteName(AppDestination.BlockAppsNotification)
            waitForIdle()
            onNodeWithTag(UiTestTags.BLOCK_APPS_NOTIFICATION_BUTTON).assertIsNotDisplayed()
        }
    }


    private fun NavController.assertCurrentRouteName(route: Any) {
        assertTrue(currentBackStackEntry?.destination?.hierarchy?.any { it.hasRoute(route::class) } == true)
    }
}