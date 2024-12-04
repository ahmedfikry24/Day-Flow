package com.example.dayflow.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

abstract class BaseViewModel<U, E>(uiState: U) : ViewModel() {

    protected val _state = MutableStateFlow(uiState)
    val state = _state
        .onStart { initData() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = uiState
        )

    private val _events = MutableSharedFlow<E>()
    val events = _events.asSharedFlow()

    abstract fun initData()

    fun <T> tryExecute(
        call: suspend () -> T,
        onSuccess: (T) -> Unit,
        onError: () -> Unit,
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
    ) {
        val handler = CoroutineExceptionHandler { _, error ->
            onError()
        }
        viewModelScope.launch(dispatcher + handler) {
            val result = call()
            onSuccess(result)
        }
    }

    protected fun sendEvent(event: E) {
        viewModelScope.launch {
            _events.emit(event)
        }
    }
}

