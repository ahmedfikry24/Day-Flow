package com.example.dayflow.navigation

import kotlinx.serialization.Serializable

sealed interface AppDestination {

    @Serializable
    data object DailyTasks : AppDestination

    @Serializable
    data object YearlyGoals : AppDestination

    @Serializable
    data object WorkSessions : AppDestination

    @Serializable
    data object Settings : AppDestination

    @Serializable
    data object BlockAppsNotification : AppDestination
}
