package com.feylabs.sawitjaya.ui.rs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feylabs.sawitjaya.data.SawitRepository
import com.feylabs.sawitjaya.data.remote.request.RequestSellRequest
import com.feylabs.sawitjaya.utils.service.Resource
import kotlinx.coroutines.launch

class RsDetailViewModel(
    val sawitRepository: SawitRepository
) : ViewModel() {

    var _uploadRDS = MutableLiveData<Resource<String?>>()
    val uploadRDS: LiveData<Resource<String?>> get() = _uploadRDS

    fun uploadData(requestSellRequest: RequestSellRequest) =
        sawitRepository.uploadRequestSell(requestSellRequest)



}