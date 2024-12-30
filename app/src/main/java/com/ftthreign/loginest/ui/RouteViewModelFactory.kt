package com.ftthreign.loginest.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ftthreign.loginest.data.repository.RouteRepository
import com.ftthreign.loginest.di.Injection
import com.ftthreign.loginest.ui.addroute.AddRouteViewModel
import com.ftthreign.loginest.ui.detail.DetailViewModel
import com.ftthreign.loginest.ui.main.done.DoneViewModel
import com.ftthreign.loginest.ui.main.todo.TodoViewModel

class RouteViewModelFactory(private val routeRepository: RouteRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(TodoViewModel::class.java) -> {
                TodoViewModel(routeRepository) as T
            }
            modelClass.isAssignableFrom(DoneViewModel::class.java) -> {
                DoneViewModel(routeRepository) as T
            }
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                DetailViewModel(routeRepository) as T
            }
            modelClass.isAssignableFrom(AddRouteViewModel::class.java) -> {
                AddRouteViewModel(routeRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: RouteViewModelFactory? = null
        @JvmStatic
        fun getInstanceRoute(context: Context): RouteViewModelFactory {
            if (INSTANCE == null) {
                synchronized(RouteViewModelFactory::class.java) {
                    INSTANCE = RouteViewModelFactory(Injection.provideRouteRepository(context))
                }
            }
            return INSTANCE as RouteViewModelFactory
        }
    }
}