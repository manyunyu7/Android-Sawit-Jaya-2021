package com.feylabs.sawitjaya.injection

import android.content.Context
import com.feylabs.sawitjaya.ViewModelFactory
import com.feylabs.sawitjaya.data.AuthRepository
import com.feylabs.sawitjaya.data.SawitRepository
import com.feylabs.sawitjaya.data.local.LocalDataSource
import com.feylabs.sawitjaya.data.local.room.MyRoomDatabase
import com.feylabs.sawitjaya.data.remote.RemoteDataSource
import com.feylabs.sawitjaya.utils.service.ApiClient

object ServiceLocator {

//    const val REAL_URL = "https://sawit-jaya.feylabs.my.id/"
//    const val BASE_URL = "${REAL_URL}api/"

    const val REAL_URL = "http://192.168.1.161:3202/"
    const val BASE_URL = "${REAL_URL}api/"

    fun provideAuthRepository(context: Context): AuthRepository {
        val apiService = ApiClient.getClient(context)
        val remoteDataSource = RemoteDataSource(apiService, context)
        val roomDb = MyRoomDatabase.invoke(context)
        val localDataSource = LocalDataSource(roomDb)
        val authRepository = AuthRepository(remoteDataSource, localDataSource)

        return authRepository
    }

    fun provideSawitRepository(context: Context): SawitRepository {
        val apiService = ApiClient.getClient(context)
        val remoteDataSource = RemoteDataSource(apiService, context)
        val roomDb = MyRoomDatabase.invoke(context)
        val localDataSource = LocalDataSource(roomDb)
        val repo = SawitRepository(remoteDataSource, localDataSource)

        return repo
    }


    fun provideFactory(context: Context): ViewModelFactory {
        val apiService = ApiClient.getClient(context)
        val remoteDataSource = RemoteDataSource(apiService, context)
        val roomDb = MyRoomDatabase.invoke(context)
        val localDataSource = LocalDataSource(roomDb)
        val authRepository = AuthRepository(remoteDataSource, localDataSource)
        val sawitRepository = SawitRepository(remoteDataSource, localDataSource)

        return ViewModelFactory(sawitRepository, authRepository)
    }


}