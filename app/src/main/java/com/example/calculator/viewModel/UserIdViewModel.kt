package com.example.calculator.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

object UserIdViewModel: ViewModel() {
    private val _stateUserId = MutableStateFlow("0")
    val stateUserId: StateFlow<String> = _stateUserId

    fun loadUserId(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _stateUserId.emit(userId)
        }
    }
}
