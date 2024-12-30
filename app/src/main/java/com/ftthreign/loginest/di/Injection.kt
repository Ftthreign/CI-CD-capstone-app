package com.ftthreign.loginest.di

import android.content.Context
import com.ftthreign.loginest.data.repository.RouteRepository
import com.ftthreign.loginest.data.repository.UserRepository
import com.ftthreign.loginest.data.source.datastore.UserPreference
import com.ftthreign.loginest.data.source.datastore.dataStore
import com.ftthreign.loginest.data.source.remote.retrofit.ApiConfig

object Injection {
    fun provideUserRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService(pref)
        return UserRepository.getInstance(pref, apiService)
    }

    fun provideRouteRepository(context: Context): RouteRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService(pref)
        return RouteRepository.getInstance(apiService)
    }
}