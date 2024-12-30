package com.ftthreign.loginest.ui.welcome

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ftthreign.loginest.data.repository.UserRepository
import com.ftthreign.loginest.data.source.datastore.UserModel

class WelcomeViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun getSession(): LiveData<UserModel> {
        return userRepository.getSession().asLiveData()
    }
}