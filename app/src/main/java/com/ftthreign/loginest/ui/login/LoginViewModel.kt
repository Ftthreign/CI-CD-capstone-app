package com.ftthreign.loginest.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ftthreign.loginest.data.repository.UserRepository
import com.ftthreign.loginest.data.source.datastore.UserModel
import kotlinx.coroutines.launch

class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun login(name: String, pass: String) = userRepository.login(name, pass)

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            userRepository.saveSession(user)
        }
    }
}