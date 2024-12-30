package com.ftthreign.loginest.ui.detail

import androidx.lifecycle.ViewModel
import com.ftthreign.loginest.data.repository.RouteRepository
import com.google.android.gms.maps.model.LatLng

class DetailViewModel(private val routeRepository: RouteRepository) : ViewModel() {
    fun getDetailRoute(id: String, status: String) = routeRepository.getDetailRoute(id, status)

    fun updateRoute(id: String) = routeRepository.updateRoute(id)

    fun getRoute(origin: LatLng, destination: LatLng, waypoints: List<LatLng>) =
        routeRepository.getRoute(origin, destination, waypoints)
}