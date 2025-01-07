package com.example.dayflow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dayflow.data.local.data_store.DataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MainActivityUiState(
    val isLightTheme: Boolean = true
)

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    private val _state = MutableStateFlow(MainActivityUiState())
    val state = _state.asStateFlow()

    var initialDataScope = CoroutineScope(Dispatchers.Default)

    init {
        initData()
        observeThemeState()
    }

    private fun initData() {
        initialDataScope.launch {
            dataStoreManager.isLightTheme.collect { targetState ->
                _state.update { it.copy(isLightTheme = targetState) }
            }
        }
    }

    private fun observeThemeState() {
        viewModelScope.launch {
            dataStoreManager.isLightTheme
                .shareIn(scope = this, started = SharingStarted.Eagerly)
                .collect { targetState ->
                    _state.update { it.copy(isLightTheme = targetState) }
                }
        }
    }
}
