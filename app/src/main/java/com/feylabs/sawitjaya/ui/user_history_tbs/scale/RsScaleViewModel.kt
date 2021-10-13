package com.feylabs.sawitjaya.ui.user_history_tbs.scale

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feylabs.sawitjaya.data.SawitRepository
import com.feylabs.sawitjaya.data.remote.response.GetRsScaleByIDResponse
import com.feylabs.sawitjaya.utils.service.Resource
import kotlinx.coroutines.launch
import org.json.JSONObject
import timber.log.Timber
import java.lang.Exception

class RsScaleViewModel(val sawitRepository: SawitRepository) : ViewModel() {

    var _scaleLiveData = MutableLiveData<Resource<GetRsScaleByIDResponse>>()
    val scaleLiveData get() = _scaleLiveData as LiveData<Resource<GetRsScaleByIDResponse>>

    var _storeScaleLiveData = MutableLiveData<Resource<String>>()
    val storeScaleLiveData get() = _storeScaleLiveData as LiveData<Resource<String>>

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

    fun storeScaleLiveData(
        rsID: String,
        result: String,
        createdBy: String
    ) {
        viewModelScope.launch {
            _scaleLiveData.value = Resource.Loading()
            try {
                val res = sawitRepository.storeScaleDataByRsId(
                    rsId = rsID,
                    result = result,
                    createdBy = createdBy
                )
                val message = res.body()?.messageString.toString()
                if (res.isSuccessful) {
                    _storeScaleLiveData.value = Resource.Success(message)
                } else {
                    _storeScaleLiveData.value = Resource.Error(res.message())
                }

            } catch (e: Exception) {
                _storeScaleLiveData.value = Resource.Error(e.message.toString())
            }

        }
    }

}