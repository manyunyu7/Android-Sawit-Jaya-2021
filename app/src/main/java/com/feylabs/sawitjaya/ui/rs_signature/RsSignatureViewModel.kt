package com.feylabs.sawitjaya.ui.rs_signature

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
import java.io.File
import java.lang.Exception

class RsSignatureViewModel(private val sawitRepository: SawitRepository) : ViewModel() {


    private var _storeSignatureLD: MutableLiveData<Resource<String?>> =
        MutableLiveData<Resource<String?>>()

    private var _detailRsLD = MutableLiveData<Resource<HistoryDetailResponse?>>()
    val detailRsLD get() = _detailRsLD as LiveData<Resource<HistoryDetailResponse?>>

    var _scaleLiveData = MutableLiveData<Resource<GetRsScaleByIDResponse>>()
    val scaleLiveData get() = _scaleLiveData as LiveData<Resource<GetRsScaleByIDResponse>>



    val storeSignatureLD get() = _storeSignatureLD

    fun upload(
        rsId: String,
        type: String,
        file: File
    ) = sawitRepository.storeRequestSellSignature(rsId = rsId, type = type, file = file)

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


    fun getDetail(id: String) {
        _detailRsLD.postValue(Resource.Loading())
        viewModelScope.launch {
            try {
                val request = sawitRepository.getDetailRs(id)
                if (request.isSuccessful) {
                    _detailRsLD.postValue(Resource.Success(request.body()))
                } else {
                    _detailRsLD.postValue(Resource.Error(request.message()))
                }
            } catch (e: Exception) {
                _detailRsLD.postValue(Resource.Error(e.toString()))
            }
        }
    }
}