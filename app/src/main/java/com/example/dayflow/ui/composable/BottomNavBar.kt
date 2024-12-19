package com.example.dayflow.ui.composable

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.dayflow.R
import com.example.dayflow.navigation.AppDestination

private data class BottomNavItem<T : Any>(
    val name: String,
    val route: T,
    @DrawableRes val icon: Int,
)

@Composable
fun BottomNavBar(navController: NavController) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val screens = listOf(
        BottomNavItem(
            stringResource(R.string.daily),
            AppDestination.DailyTasks,
            R.drawable.daily_tasks
        ),
        BottomNavItem(
            stringResource(R.string.yearly),
            AppDestination.YearlyGoals,
            R.drawable.yearly_goals
        ),
    )

    NavigationBar(
        modifier = Modifier.fillMaxWidth(),
        containerColor = MaterialTheme.colorScheme.background
    ) {
        screens.forEach { screen ->
            val isSelected =
                currentDestination?.hierarchy?.any { it.hasRoute(screen.route::class) } == true
            val contentColor =
                if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
            this.NavigationBarItem(
                selected = isSelected,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        ImageVector.vectorResource(screen.icon),
                        contentDescription = null,
                        tint = contentColor
                    )
                },
                label = {
                    Text(
                        screen.name,
                        style = MaterialTheme.typography.titleSmall,
                        color = contentColor
                    )
                },
            )
        }
    }
}