package com.example.dayflow.data.local.data_store

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.example.dayflow.data.utils.DataConstants
import kotlinx.coroutines.flow.map
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DataConstants.DATA_STORE_NAME)

class DataStoreManager @Inject constructor(private val context: Context) {

    private var themeStatusKey = booleanPreferencesKey(DataConstants.THEME_STATUS_KEY)
    val isLightTheme = context.dataStore.data.map { preferences ->
        preferences[themeStatusKey] ?: true
    }

    suspend fun toggleThemeStatus() {
        context.dataStore.edit { preferences ->
            val currentState = preferences[themeStatusKey] ?: true
            preferences[themeStatusKey] = !currentState
        }
    }
}