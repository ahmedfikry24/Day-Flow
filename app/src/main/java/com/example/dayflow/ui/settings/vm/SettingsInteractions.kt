package com.example.dayflow.ui.settings.vm

import android.net.Uri

interface SettingsInteractions {
    fun initData()
    fun onClickBlockApps()
    fun onToggleTheme()
    fun saveRingtoneUri(uri: Uri)
}
