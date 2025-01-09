package com.example.dayflow.data.usecase

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.AdaptiveIconDrawable
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.VectorDrawable
import com.example.dayflow.data.local.entity.BlockAppInfoEntity
import com.example.dayflow.data.repository.Repository
import javax.inject.Inject

class GetAllInstalledAppsUseCase @Inject constructor(
    private val repository: Repository,
    context: Context
) {
    private val packageManager = context.packageManager

    suspend operator fun invoke(): List<BlockAppInfoEntity> {
        val installedApps = getAllInstalledApps().map { it.toEntity() }
        val blockedApps = repository.getAllBlockedApps()
        installedApps.forEach { app ->
            blockedApps.forEach { blockedApp ->
                val indexOfBlockApp = blockedApps.indexOfFirst { blockedApp.id == app.id }
                val indexOfInstalledApp = installedApps.indexOfFirst { blockedApp.id == app.id }
                if (indexOfBlockApp != -1 && indexOfInstalledApp != -1) {
                    val blockedAppItem = blockedApps[indexOfBlockApp]
                    installedApps.toMutableList()[indexOfInstalledApp] = blockedAppItem
                }
            }
        }

        return listOf()
    }

    private fun getAllInstalledApps(): List<ApplicationInfo> {
        return packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
            .filter { app -> (app.flags and ApplicationInfo.FLAG_SYSTEM) == 0 }
    }

    private fun ApplicationInfo.toEntity(): BlockAppInfoEntity {
        return BlockAppInfoEntity(
            id = this.uid,
            name = this.loadLabel(packageManager).toString(),
            icon = this.loadIcon(packageManager).convertToBitmap(),
            isBlock = false
        )
    }


    private fun Drawable.convertToBitmap(): Bitmap {
        return when (val drawable = this) {
            is AdaptiveIconDrawable -> {
                if (drawable.background == null && drawable.foreground == null) {
                    val width = drawable.intrinsicWidth
                    val height = drawable.intrinsicHeight
                    Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888).apply {
                        val canvas = Canvas(this)
                        drawable.setBounds(0, 0, width, height)
                        drawable.draw(canvas)
                    }
                } else {
                    val width = drawable.intrinsicWidth
                    val height = drawable.intrinsicHeight
                    Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888).apply {
                        val canvas = Canvas(this)
                        drawable.background?.setBounds(0, 0, width, height)
                        drawable.background?.draw(canvas)
                        drawable.foreground?.setBounds(0, 0, width, height)
                        drawable.foreground?.draw(canvas)
                    }
                }
            }

            is VectorDrawable -> {
                val width = drawable.intrinsicWidth
                val height = drawable.intrinsicHeight
                Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888).apply {
                    val canvas = Canvas(this)
                    drawable.setBounds(0, 0, width, height)
                    drawable.draw(canvas)
                }
            }

            is BitmapDrawable -> drawable.bitmap

            else -> Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
        }
    }
}