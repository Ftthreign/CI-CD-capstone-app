package com.ftthreign.loginest.ui.addroute

import androidx.lifecycle.ViewModel
import com.ftthreign.loginest.data.repository.RouteRepository
import com.ftthreign.loginest.data.source.remote.response.OptimizeRequest

class AddRouteViewModel(
    private val routeRepository: RouteRepository
) : ViewModel() {
    fun optimizeRoute(request : OptimizeRequest) = routeRepository.optimizeRoute(request)
}

