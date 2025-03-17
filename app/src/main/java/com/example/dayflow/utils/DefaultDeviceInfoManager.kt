package com.example.dayflow.utils

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.PowerManager
import com.example.dayflow.R
import com.example.dayflow.ui.utils.goToSettings

class DefaultDeviceInfoManager(private val context: Context) {

    val packageManager: PackageManager = context.packageManager

    fun getAllInstalledApps(): List<ApplicationInfo> {
        return packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
            .filter { app -> (app.flags and ApplicationInfo.FLAG_SYSTEM) == 0 }
            .filterNot { it.packageName == context.packageName }
    }

    fun isBatteryOptimizationEnabled(): Boolean {
        val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        val packageName = context.packageName
        return !powerManager.isIgnoringBatteryOptimizations(packageName)
    }

    fun showDefaultBatteryOptimizationDialog() {
        AlertDialog.Builder(context)
            .setTitle(context.getString(R.string.battery_optimization))
            .setMessage(context.getString(R.string.to_ensure_the_app_works_correctly_please_disable_battery_optimization_for_this_app_go_to_settings_battery_battery_optimization))
            .setPositiveButton(context.getString(R.string.open_settings)) { _, _ ->
                (context as Activity).goToSettings()
            }
            .setNegativeButton(context.getString(R.string.cancel), null)
            .show()
    }

}
