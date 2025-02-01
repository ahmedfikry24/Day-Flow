package com.example.dayflow.data.utils

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager

class PackageAppsManager(private val context: Context) {

    val packageManager: PackageManager = context.packageManager

    fun getAllInstalledApps(): List<ApplicationInfo> {
        return packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
            .filter { app -> (app.flags and ApplicationInfo.FLAG_SYSTEM) == 0 }
            .filterNot { it.packageName == context.packageName }
    }
}