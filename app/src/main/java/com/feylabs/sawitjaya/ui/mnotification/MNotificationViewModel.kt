package com.feylabs.sawitjaya.ui.mnotification

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feylabs.sawitjaya.data.SawitRepository
import com.feylabs.sawitjaya.data.remote.response.MNotificationResponse
import com.feylabs.sawitjaya.utils.service.Resource
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception

class MNotificationViewModel(
    val sawitRepository: SawitRepository
) : ViewModel(
) {
    private var _mNotifLD = MutableLiveData<Resource<MNotificationResponse>>()
    val mNotifLiveData: LiveData<Resource<MNotificationResponse>>
        get() = _mNotifLD

    fun fetchNotification(userId: String) {
        viewModelScope.launch {
            _mNotifLD.postValue(Resource.Loading())
            try {
                val res = sawitRepository.getMNotificationByUser(userId)
                Timber.d(("fetch notif $res"))
                if (res.isSuccessful) {
                    res.body()?.let {
                        _mNotifLD.postValue(Resource.Success(it))
                    }
                } else {
                    _mNotifLD.postValue(Resource.Error("Terjadi Kesalahan"))
                }
            } catch (e: Exception) {
                _mNotifLD.postValue(Resource.Error("Terjadi Error : ${e.message}"))
            }
        }
    }
}