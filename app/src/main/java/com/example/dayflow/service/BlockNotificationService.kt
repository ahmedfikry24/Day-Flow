package com.example.dayflow.service

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import com.example.dayflow.data.local.entity.BlockAppInfoEntity
import com.example.dayflow.data.usecase.GetAllInstalledAppsUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

var isBlockNotificationListenerActive = false

@AndroidEntryPoint
class BlockNotificationService : NotificationListenerService() {

    @Inject
    lateinit var getAllInstalledAppsUseCase: GetAllInstalledAppsUseCase
    private val coroutineScope = CoroutineScope(Dispatchers.Default)
    private var blockedApps = listOf<BlockAppInfoEntity>()

    override fun onCreate() {
        super.onCreate()
        coroutineScope.launch {
            blockedApps = getAllInstalledAppsUseCase()
        }
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)
        if (isBlockNotificationListenerActive) {
            sbn?.let { it.packageName.cancelAppNotification(it.key) }
        }
    }

    private fun String.cancelAppNotification(key: String) {
        if (blockedApps.any { this == it.packageName }) {
            cancelNotification(key)
        }
    }
}