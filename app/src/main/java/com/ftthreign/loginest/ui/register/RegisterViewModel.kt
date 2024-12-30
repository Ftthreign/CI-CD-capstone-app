package com.ftthreign.loginest.ui.register

import androidx.lifecycle.ViewModel
import com.ftthreign.loginest.data.repository.UserRepository

class RegisterViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun register(name: String, pass: String, retypePass: String) = userRepository.register(name, pass, retypePass)
}