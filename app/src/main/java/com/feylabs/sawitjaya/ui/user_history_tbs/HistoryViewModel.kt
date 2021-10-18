package com.feylabs.sawitjaya.ui.user_history_tbs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feylabs.sawitjaya.data.SawitRepository
import com.feylabs.sawitjaya.data.remote.response.GetRequestSellByUserReq
import com.feylabs.sawitjaya.data.remote.response.HistoryDataResponse
import com.feylabs.sawitjaya.data.remote.response.HistoryDetailResponse
import com.feylabs.sawitjaya.ui.user_history_tbs.HistoryPagingModel.HistoryModel
import com.feylabs.sawitjaya.data.remote.service.Resource
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception

class HistoryViewModel(
    val sawitRepository: SawitRepository
) : ViewModel() {

    var filterStatus: MutableLiveData<String> = MutableLiveData(null);

    var _historyDataLD =
        MutableLiveData<Resource<HistoryDataResponse?>>()

    val historyDataLD: LiveData<Resource<HistoryDataResponse?>>
        get() = _historyDataLD

    fun getRSByUser(
        userID: String, paginate: Boolean = true,
        page: Int = 1, perPage: Int = 10, status: String? = filterStatus.value
    ) {
        _historyDataLD.postValue(Resource.Loading())
        viewModelScope.launch {
            try {
                val request = sawitRepository.getRequestSellByUser(
                    userID = userID, paginate = paginate, page = page, per_page = perPage,
                    status = status
                )
                val mBody = request.body()
                if (request.isSuccessful) {
                    _historyDataLD.postValue(Resource.Success(mBody))
                } else {
                    _historyDataLD.postValue(Resource.Error("Gagal Mengambil Data"))
                }
            } catch (e: Exception) {
                _historyDataLD.postValue(Resource.Error("Terjadi Kesalahan : ${e.message}"))
            }
        }
    }


}