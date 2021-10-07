package com.feylabs.sawitjaya.ui.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feylabs.sawitjaya.data.SawitRepository
import com.feylabs.sawitjaya.data.remote.request.RsChatStoreRequestBody
import com.feylabs.sawitjaya.data.remote.response.RsChatResponse
import com.feylabs.sawitjaya.utils.service.Resource
import com.google.gson.Gson
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RsChatViewModel(val sawitRepository: SawitRepository) : ViewModel() {

    private var _chatItemLD = MutableLiveData<Resource<RsChatResponse?>>()
    val chatItemLD get() = _chatItemLD as LiveData<Resource<RsChatResponse?>>

    private var _insertChatItemLD = MutableLiveData<Resource<String?>>()
    val insertChatItemLD get() = _insertChatItemLD as LiveData<Resource<String?>>

    fun fetchChat(topicId: String) {
        _chatItemLD.value = Resource.Loading()
        viewModelScope.launch {
            try {
                val req = sawitRepository.getChatByTopic(topicId)
                Timber.d("log detailx ${req.body().toString()}")
                if (req.isSuccessful) {
                    _chatItemLD.postValue(Resource.Success(req.body()))
                } else {
                    _chatItemLD.postValue(Resource.Error(req.message()))
                }
            } catch (e: Exception) {
                Timber.d("log detailx ${e.toString()}")
                Timber.d("log detailx ${e.message}")
                Timber.d("log detailx ${e.stackTrace}")
                _chatItemLD.postValue(Resource.Error(e.toString()))
            }
        }
    }

    fun insertChat(chatStoreRequestBody: RsChatStoreRequestBody) {
        viewModelScope.launch {
            val req = sawitRepository.insertChat(chatStoreRequestBody)
            req.enqueue(object : Callback<ResponseBody?> {
                override fun onResponse(
                    call: Call<ResponseBody?>,
                    response: Response<ResponseBody?>
                ) {
                    if (response.code() == 200) {
                        _insertChatItemLD.value = Resource.Success("Berhasil")
                    } else {
                        _insertChatItemLD.value = Resource.Error("Gagal")
                    }
                }

                override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                    _insertChatItemLD.value = Resource.Error(t.message.toString())
                }
            })
        }
    }

}