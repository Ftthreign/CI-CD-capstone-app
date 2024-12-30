package com.ftthreign.loginest.ui.main.todo

import androidx.lifecycle.ViewModel
import com.ftthreign.loginest.data.repository.RouteRepository
import com.google.android.gms.maps.model.LatLng

class TodoViewModel(private val routeRepository: RouteRepository) : ViewModel() {
    fun getUnfinishedRoute() = routeRepository.getUnfinishedRoute()

    fun getRoute(origin: LatLng, destination: LatLng, waypoints: List<LatLng>) =
        routeRepository.getRoute(origin, destination, waypoints)
}