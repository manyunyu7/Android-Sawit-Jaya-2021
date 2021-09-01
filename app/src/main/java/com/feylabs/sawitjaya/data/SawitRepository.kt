package com.feylabs.sawitjaya.data

import androidx.lifecycle.MutableLiveData as MLD
import com.feylabs.sawitjaya.data.local.LocalDataSource
import com.feylabs.sawitjaya.data.local.room.entity.AuthEntity
import com.feylabs.sawitjaya.data.local.room.entity.NewsEntity
import com.feylabs.sawitjaya.data.remote.RemoteDataSource as remote
import com.feylabs.sawitjaya.data.remote.request.RegisterRequestBody
import com.feylabs.sawitjaya.data.remote.response.ChangePasswordResponse
import com.feylabs.sawitjaya.data.remote.response.UserUpdateProfileResponse
import java.io.File
import com.feylabs.sawitjaya.service.Resource as Res

class SawitRepository(
    private val remoteDs: remote,
    private val localDs: LocalDataSource,
) {

    fun saveNewJWTToken(token: String) {
        localDs.saveNewJWTToken(token)
    }

    suspend fun getNews() = remoteDs.getNews()
    suspend fun getPrices() = remoteDs.getPrices()
    suspend fun insertNews(newsEntity: NewsEntity) = localDs.saveNews(newsEntity)

}