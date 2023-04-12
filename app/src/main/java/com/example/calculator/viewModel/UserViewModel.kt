package com.example.calculator.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calculator.DTO.UserDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


object UserViewModel: ViewModel() {
    private val _stateUser = MutableStateFlow<UserDTO?>(null)
    val stateUser: StateFlow<UserDTO?> = _stateUser

    fun loadUser(userDTO: UserDTO) {
        viewModelScope.launch(Dispatchers.IO) {
            _stateUser.emit(userDTO)
        }
    }
}