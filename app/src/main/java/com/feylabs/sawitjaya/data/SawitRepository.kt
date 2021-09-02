package com.feylabs.sawitjaya.data

import androidx.lifecycle.MutableLiveData as MLD
import com.feylabs.sawitjaya.data.local.LocalDataSource
import com.feylabs.sawitjaya.data.local.room.entity.NewsEntity
import com.feylabs.sawitjaya.data.local.room.entity.PriceResponseEntity
import com.feylabs.sawitjaya.data.remote.RemoteDataSource as remote

class SawitRepository(
    private val remoteDs: remote,
    private val localDs: LocalDataSource,
) {

    fun saveNewJWTToken(token: String) {
        localDs.saveNewJWTToken(token)
    }

    suspend fun getNews() = remoteDs.getNews()
    suspend fun getPrices() = remoteDs.getPrices()

    suspend fun getPricesLocally() = localDs.getPrice()
    suspend fun getNewsLocally() = localDs.getNews()

    suspend fun insertNews(newsEntity: NewsEntity) = localDs.saveNews(newsEntity)
    suspend fun insertPrice(priceResponseEntity: PriceResponseEntity) =
        localDs.savePrices(priceResponseEntity)

}