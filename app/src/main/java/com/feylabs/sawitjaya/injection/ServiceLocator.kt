package com.feylabs.sawitjaya.injection

import android.content.Context
import com.feylabs.sawitjaya.ViewModelFactory
import com.feylabs.sawitjaya.data.AuthRepository
import com.feylabs.sawitjaya.data.remote.RemoteDataSource
import com.feylabs.sawitjaya.service.ApiService
import com.feylabs.sawitjaya.service.MainEndpoint

object ServiceLocator {

     fun provideAuthRepository(context: Context) : AuthRepository{
        val apiService = ApiService.getClient(context)
        val remoteDataSource = RemoteDataSource(apiService)
        val authRepository = AuthRepository(remoteDataSource)

        return authRepository
    }

    fun provideFactory(context: Context): ViewModelFactory {
        val apiService = ApiService.getClient(context)
        val remoteDataSource = RemoteDataSource(apiService)
        val authRepository = AuthRepository(remoteDataSource)

        return ViewModelFactory(authRepository)
    }


}