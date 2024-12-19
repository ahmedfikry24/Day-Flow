package com.example.dayflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.dayflow.navigation.AppDestination
import com.example.dayflow.ui.composable.BottomNavBar
import com.example.dayflow.ui.daily_tasks.DailyTasksScreen
import com.example.dayflow.ui.theme.DayFlowTheme
import com.example.dayflow.ui.yearly_tasks.YearlyTasksScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        installSplashScreen()
        setContent {
            DayFlowTheme {
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
                        }
                    }
                }
            }
        }
    }
}