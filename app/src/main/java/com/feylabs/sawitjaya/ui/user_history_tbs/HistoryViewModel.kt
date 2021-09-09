package com.feylabs.sawitjaya.ui.user_history_tbs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feylabs.sawitjaya.data.SawitRepository
import com.feylabs.sawitjaya.data.remote.response.GetRequestSellByUserReq
import com.feylabs.sawitjaya.ui.user_history_tbs.HistoryPagingModel.HistoryModel
import com.feylabs.sawitjaya.utils.service.Resource
import kotlinx.coroutines.launch
import java.lang.Exception

class HistoryViewModel(
    val sawitRepository: SawitRepository
) : ViewModel() {


    var _historyDataLD =
        MutableLiveData<Resource<HistoryPagingModel>>()

    val historyDataLD: LiveData<Resource<HistoryPagingModel>>
        get() = _historyDataLD

    fun getRSByUser(
        userID: String, paginate: Boolean = true,
        page: Int = 1, perPage: Int = 10
    ) {
        viewModelScope.launch {
            val request = sawitRepository.getRequestSellByUser(
                userID = userID, paginate = paginate, page = page, per_page = perPage
            )
            try {
                val mBody = historyResponseDataToHistoryModelMapper(request.body()!!)
                if (request.isSuccessful) {
                    _historyDataLD?.postValue(Resource.Success(mBody))
                } else {
                    _historyDataLD?.postValue(Resource.Error("Gagal Mengambil Data"))
                }
            } catch (e: Exception) {
                _historyDataLD?.postValue(Resource.Error("Terjadi Kesalahan : ${e.message}"))
            }

        }
    }


    fun historyResponseDataToHistoryModelMapper(
        response: GetRequestSellByUserReq
    ): HistoryPagingModel {

        val temp = mutableListOf<HistoryModel>()
        response.data.forEach {
            temp.add(
                HistoryModel(
                    id = it.id,
                    userId = it.userId,
                    address = it.address,
                    contact = it.contact,
                    createdAt = it.createdAt,
                    driverId = it.driverId,
                    estMargin = it.estMargin,
                    estPrice = it.estPrice,
                    estWeight = it.estWeight,
                    lat = it.lat,
                    long = it.long,
                    userPhoto = it.userPhoto,
                    photo = it.photoPath,
                    staffId = it.staffId,
                    status = it.status,
                    statusDesc = it.statusDesc,
                    driverName = it.driverName,
                    staffName = it.staffName,
                    updatedAt = it.updatedAt,
                    updatedBy = it.updatedBy,
                    userName = it.userName
                )
            )
        }

        val pagingModel = HistoryPagingModel(
            perPage = response.perPage,
            data = temp,
            currentPage = response.currentPage
        )
        return pagingModel
    }


}