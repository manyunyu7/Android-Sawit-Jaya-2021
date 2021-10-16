package com.feylabs.sawitjaya.data

import androidx.lifecycle.MutableLiveData as MLD
import com.feylabs.sawitjaya.data.local.LocalDataSource
import com.feylabs.sawitjaya.data.local.room.entity.AuthEntity
import com.feylabs.sawitjaya.data.local.room.entity.NewsEntity
import com.feylabs.sawitjaya.data.local.room.entity.PriceResponseEntity
import com.feylabs.sawitjaya.data.remote.request.RequestSellRequest
import com.feylabs.sawitjaya.data.remote.request.RsChatStoreRequestBody
import com.feylabs.sawitjaya.data.remote.service.Resource
import java.io.File
import com.feylabs.sawitjaya.data.remote.RemoteDataSource as remote

class SawitRepository(
    private val remoteDs: remote,
    private val localDs: LocalDataSource,
) {


    suspend fun getUserID() = localDs.getUserID()
    suspend fun getToken() = localDs.getUserID()

    suspend fun getNews() = remoteDs.getNews()
    suspend fun getPrices() = remoteDs.getPrices()
    suspend fun getDetailRs(id: String) = remoteDs.getDetailRequestSell(id)
    suspend fun getMNotificationByUser(userID: String) = remoteDs.getMNotificationByUser(userID)
    suspend fun getChatByTopic(topicId: String) = remoteDs.getRsChatByTopic(topicId = topicId)


    suspend fun getRequestSellByUser(
        userID: String,
        page: Int,
        status: String? = null,
        per_page: Int,
        paginate: Boolean = true
    ) =
        remoteDs.getRequestSellByUser(
            userID = userID,
            per_page = per_page,
            page = page,
            paginate = paginate,
            status = status
        )

    suspend fun getPricesLocally() = localDs.getPrice()
    suspend fun getNewsLocally() = localDs.getNews()
    suspend fun clearNewsLocally() = localDs.clearNews()

    suspend fun insertNews(newsEntity: NewsEntity) = localDs.saveNews(newsEntity)

    suspend fun getScaleDataByRsId(rsId: String) = remoteDs.getScaleDataByRsID(rsId)

    suspend fun deleteScaleDataById(rsId: String) = remoteDs.deleteScaleDataByID(rsId)

    suspend fun storeScaleDataByRsId(rsId: String, result: String, createdBy: String) =
        remoteDs.storeScaleDataByRsID(rsId, result, createdBy)

    suspend fun insertPrice(priceResponseEntity: PriceResponseEntity) =
        localDs.savePrices(priceResponseEntity)

    fun uploadRequestSell(rsReq: RequestSellRequest): MLD<Resource<String?>> {
        val _response = MLD<Resource<String?>>()
        remoteDs.sendRequestSell(rsReq, object : remote.CallbackUploadRS {
            override fun value(response: Resource<String?>) {
                _response.postValue(response)
            }
        })

        return _response
    }

    suspend fun storeFinalRsData(
        rsId: String, finalPrice: String, finalMargin: String, pricePaid: String
    ) = remoteDs.storeRsFinalInfo(
        rsId,
        finalPrice = finalPrice,
        finalMargin = finalMargin,
        pricePaid = pricePaid
    )

    fun storeRequestSellSignature(
        rsId: String,
        type: String,
        file: File
    ): MLD<Resource<String?>> {
        val _response = MLD<Resource<String?>>()
        remoteDs.storeRsFileSignature(
            rsID = rsId, type = type, file = file, object : remote.CallbackStoreRsSignature {
                override fun value(response: Resource<String?>) {
                    _response.postValue(response)
                }

            })

        return _response
    }

    suspend fun insertChat(chatStoreRequestBody: RsChatStoreRequestBody) = remoteDs.insertChat(
        chatStoreRequestBody
    )

    suspend fun changeRsStatus(rsId: String, status: String) =
        remoteDs.changeRsStatus(rsId, status)

    suspend fun localSaveAuthInfo(authEntity: AuthEntity) {
        localDs.saveAuthInfo(authEntity)
    }

    suspend fun refreshToken() = remoteDs.refreshToken()

}