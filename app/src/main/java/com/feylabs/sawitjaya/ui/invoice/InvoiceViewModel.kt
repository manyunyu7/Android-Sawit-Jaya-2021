package com.feylabs.sawitjaya.ui.invoice

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feylabs.sawitjaya.data.SawitRepository
import com.feylabs.sawitjaya.data.remote.response.GetRsScaleByIDResponse
import com.feylabs.sawitjaya.data.remote.response.HistoryDetailResponse
import com.feylabs.sawitjaya.data.remote.service.Resource
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception

class InvoiceViewModel(val sawitRepository: SawitRepository) : ViewModel() {

    private var _detailRsLD = MutableLiveData<Resource<HistoryDetailResponse?>>()
    val detailRsLD get() = _detailRsLD as LiveData<Resource<HistoryDetailResponse?>>

    var _scaleLiveData = MutableLiveData<Resource<GetRsScaleByIDResponse>>()
    val scaleLiveData get() = _scaleLiveData as LiveData<Resource<GetRsScaleByIDResponse>>


    fun getDetail(id: String) {
        _detailRsLD.postValue(Resource.Loading())
        viewModelScope.launch {
            try {
                val request = sawitRepository.getDetailRs(id)
                Timber.d("log detailx ${request.body().toString()}")
                if (request.isSuccessful) {
                    _detailRsLD.postValue(Resource.Success(request.body()))
                } else {
                    _detailRsLD.postValue(Resource.Error(request.message()))
                }
            } catch (e: Exception) {
                Timber.d("log detailx ${e.message}")
                Timber.d("log detailx ${e.stackTrace}")
                _detailRsLD.postValue(Resource.Error(e.toString()))
            }
        }
    }

    fun fetchScaleData(rsID: String) {
        viewModelScope.launch {
            _scaleLiveData.value = Resource.Loading()
            try {
                val res = sawitRepository.getScaleDataByRsId(rsID)
                Timber.d("rs_scale $res")
                if (res.isSuccessful) {
                    _scaleLiveData.value = Resource.Success(res.body()!!)
                } else {
                    _scaleLiveData.value = Resource.Error(res.message())
                }
            } catch (e: Exception) {
                Timber.d("rs_scale ${e.toString()}")
                _scaleLiveData.value = Resource.Error(e.message.toString())
            }
        }
    }


}