package com.example.calculator.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch


object ErrorViewModel : ViewModel() {
    private val _stateError = MutableSharedFlow<String?>()
    val stateError: SharedFlow<String?> = _stateError

    fun loadError(error: String) {
        //Log.d(TAG, token.firstName)
        viewModelScope.launch(Dispatchers.IO) {
            _stateError.emit(error)
        }
    }
}