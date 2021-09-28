package com.feylabs.sawitjaya.ui.user_history_tbs.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feylabs.sawitjaya.data.SawitRepository
import com.feylabs.sawitjaya.data.remote.response.HistoryDetailResponse
import com.feylabs.sawitjaya.utils.service.Resource
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception

class DetailHistoryViewModel(
    val sawitRepository: SawitRepository
) : ViewModel() {

    val isMapReady = MutableLiveData<Boolean>()
    val mapLatLngLv = MutableLiveData<LatLng>()

    private var _detailRsLD = MutableLiveData<Resource<HistoryDetailResponse?>>()
    val detailRsLD get() = _detailRsLD as LiveData<Resource<HistoryDetailResponse>?>

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
                Timber.d("log detailx ${e.toString()}")
                Timber.d("log detailx ${e.stackTrace}")
                _detailRsLD.postValue(Resource.Error(e.toString()))
            }
        }
    }
}