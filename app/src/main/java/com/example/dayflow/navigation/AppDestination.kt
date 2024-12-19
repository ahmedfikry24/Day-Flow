package com.example.dayflow.navigation

import kotlinx.serialization.Serializable

sealed interface AppDestination {

    @Serializable
    data object DailyTasks : AppDestination
    @Serializable
    data object YearlyGoals : AppDestination
}
