package com.example.dayflow.data.local

import com.example.dayflow.data.local.dao.BlockAppsDao
import com.example.dayflow.data.local.entity.BlockAppInfoEntity
import com.example.dayflow.utils.BaseAndroidTester
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test


class BlockAppsDao : BaseAndroidTester() {

    private lateinit var blockAppsDao: BlockAppsDao

    override fun setUp() {
        super.setUp()
        blockAppsDao = roomDatabase.blockAppsDao()
    }

    @Test
    fun given_EmptyDatabase_when_getAllBlockApps_then_returnEmptyList() = runTest {

        val apps = blockAppsDao.getAllBlockedApps()

        assertTrue(apps.isEmpty())
    }

    @Test
    fun given_taskEntity_when_addBlockedApp_then_addAppSuccess() = runTest {
        val olderApps = blockAppsDao.getAllBlockedApps()

        assertTrue(olderApps.isEmpty())

        val app = BlockAppInfoEntity(
            id = 1,
            name = "faceBook",
            packageName = "com.facebook.app",
            icon = null,
            isBlock = true
        )

        blockAppsDao.addBlockedApp(app)

        val newerBlockedApps = blockAppsDao.getAllBlockedApps()

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
        blockAppsDao.addBlockedApp(app)

        val olderApps= blockAppsDao.getAllBlockedApps()
        assertTrue(olderApps.isNotEmpty())

        blockAppsDao.removeBlockedApp(app.id)

        val newerBlockedApps = blockAppsDao.getAllBlockedApps()
        assertTrue(newerBlockedApps.isEmpty())
    }
}