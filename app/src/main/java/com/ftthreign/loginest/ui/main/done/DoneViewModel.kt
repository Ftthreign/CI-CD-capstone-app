package com.ftthreign.loginest.ui.main.done

import androidx.lifecycle.ViewModel
import com.ftthreign.loginest.data.repository.RouteRepository

class DoneViewModel(private val routeRepository: RouteRepository) : ViewModel() {
    fun getFinishedRoute() = routeRepository.getFinishedRoute()
}