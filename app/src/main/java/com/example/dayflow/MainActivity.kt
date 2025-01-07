package com.example.dayflow

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.dayflow.navigation.AppDestination
import com.example.dayflow.ui.composable.BottomNavBar
import com.example.dayflow.ui.daily_tasks.DailyTasksScreen
import com.example.dayflow.ui.settings.SettingsScreen
import com.example.dayflow.ui.theme.DayFlowTheme
import com.example.dayflow.ui.work_session.WorkSessionScreen
import com.example.dayflow.ui.yearly_tasks.YearlyTasksScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.isActive

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        installSplashScreen().setKeepOnScreenCondition { !viewModel.initialDataScope.isActive }
        setContent {
            val state by viewModel.state.collectAsStateWithLifecycle()
            LaunchedEffect(state.isLightTheme) {
                enableEdgeToEdge(
                    statusBarStyle = SystemBarStyle.auto(
                        Color.TRANSPARENT,
                        Color.TRANSPARENT,
                        detectDarkMode = { !state.isLightTheme }
                    )
                )
            }
            DayFlowTheme(darkTheme = !state.isLightTheme) {
                val navController = rememberNavController()
                Scaffold(
                    bottomBar = { BottomNavBar(navController) }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background)
                            .padding(innerPadding)
                    ) {
                        NavHost(navController, startDestination = AppDestination.DailyTasks) {
                            composable<AppDestination.DailyTasks> { DailyTasksScreen(navController) }
                            composable<AppDestination.YearlyGoals> { YearlyTasksScreen(navController) }
                            composable<AppDestination.WorkSessions> { WorkSessionScreen() }
                            composable<AppDestination.Settings> { SettingsScreen(navController) }
                        }
                    }
                }
            }
        }
    }
}