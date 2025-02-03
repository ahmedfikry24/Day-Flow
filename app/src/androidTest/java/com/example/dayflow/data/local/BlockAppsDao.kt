package com.example.dayflow.data.local

import com.example.dayflow.data.local.entity.BlockAppInfoEntity
import com.example.dayflow.utils.BaseAndroidTester
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

@HiltAndroidTest
class BlockAppsDao : BaseAndroidTester() {

    @Test
    fun given_EmptyDatabase_when_getAllBlockApps_then_returnEmptyList() = runTest {

        val apps = repository.getAllBlockedApps()

        assertTrue(apps.isEmpty())
    }

    @Test
    fun given_taskEntity_when_addBlockedApp_then_addAppSuccess() = runTest {
        val olderApps = repository.getAllBlockedApps()

        assertTrue(olderApps.isEmpty())

        val app = BlockAppInfoEntity(
            id = 1,
            name = "faceBook",
            packageName = "com.facebook.app",
            icon = null,
            isBlock = true
        )

        repository.addBlockedApp(app)

        val newerBlockedApps = repository.getAllBlockedApps()

        assertEquals(1, newerBlockedApps.size)
    }

    @Test
    fun given_appId_when_deleteBlockedApp_then_removeAppSuccess() = runTest {
        val app = BlockAppInfoEntity(
            id = 1,
            name = "faceBook",
            packageName = "com.facebook.app",
            icon = null,
            isBlock = true
        )
        repository.addBlockedApp(app)

        val olderApps = repository.getAllBlockedApps()
        assertTrue(olderApps.isNotEmpty())

        repository.removeBlockedApp(app.id)

        val newerBlockedApps = repository.getAllBlockedApps()
        assertTrue(newerBlockedApps.isEmpty())
    }
}