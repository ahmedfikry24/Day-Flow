package com.example.dayflow.data.usecase

import android.content.pm.ApplicationInfo
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.AdaptiveIconDrawable
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.VectorDrawable
import androidx.core.graphics.createBitmap
import com.example.dayflow.data.local.entity.BlockAppInfoEntity
import com.example.dayflow.data.repository.Repository
import com.example.dayflow.utils.DefaultDeviceInfoManager
import javax.inject.Inject

class GetAllInstalledAppsUseCase @Inject constructor(
    private val repository: Repository,
    private val packageAppsManager: DefaultDeviceInfoManager,
) {
    suspend operator fun invoke(): List<BlockAppInfoEntity> {
        val installedApps = packageAppsManager.getAllInstalledApps()
            .map { it.toEntity() }
            .toMutableList()

        val blockedApps = repository.getAllBlockedApps()

        for (app in blockedApps) {
            val index = installedApps.indexOfFirst { it.id == app.id }
            if (index != -1) {
                installedApps[index] = app
            }
        }
        return installedApps
    }

    private fun ApplicationInfo.toEntity(): BlockAppInfoEntity {
        return BlockAppInfoEntity(
            id = this.uid,
            name = this.loadLabel(packageAppsManager.packageManager).toString(),
            packageName = this.packageName,
            icon = this.loadIcon(packageAppsManager.packageManager).convertToBitmap(),
            isBlock = false
        )
    }

    private fun Drawable.convertToBitmap(): Bitmap {
        return when (val drawable = this) {
            is AdaptiveIconDrawable -> {
                if (drawable.background == null && drawable.foreground == null) {
                    val width = drawable.intrinsicWidth
                    val height = drawable.intrinsicHeight
                    createBitmap(width, height).apply {
                        val canvas = Canvas(this)
                        drawable.setBounds(0, 0, width, height)
                        drawable.draw(canvas)
                    }
                } else {
                    val width = drawable.intrinsicWidth
                    val height = drawable.intrinsicHeight
                    createBitmap(width, height).apply {
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
                createBitmap(width, height).apply {
                    val canvas = Canvas(this)
                    drawable.setBounds(0, 0, width, height)
                    drawable.draw(canvas)
                }
            }

            is BitmapDrawable -> drawable.bitmap

            else -> createBitmap(1, 1)
        }
    }
}