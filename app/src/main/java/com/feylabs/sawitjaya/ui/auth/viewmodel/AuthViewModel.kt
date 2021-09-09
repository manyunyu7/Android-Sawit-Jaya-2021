package com.feylabs.sawitjaya.ui.auth.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feylabs.sawitjaya.data.AuthRepository
import com.feylabs.sawitjaya.data.SawitRepository
import com.feylabs.sawitjaya.data.local.room.entity.AuthEntity
import com.feylabs.sawitjaya.data.local.room.entity.NewsEntity
import com.feylabs.sawitjaya.data.local.room.entity.PriceResponseEntity
import com.feylabs.sawitjaya.data.remote.request.RegisterRequestBody
import com.feylabs.sawitjaya.data.remote.response.NewsResponse
import com.feylabs.sawitjaya.data.remote.response.PriceResponse
import com.feylabs.sawitjaya.utils.service.Resource
import kotlinx.coroutines.launch
import java.io.File

class AuthViewModel(
    private val authRepository: AuthRepository,
    private val sawitRepository: SawitRepository,
) : ViewModel() {

    val localProfileLD = MutableLiveData<AuthEntity>()

    // for remote
    val newsLiveData = MutableLiveData<Resource<NewsResponse?>>()
    val pricesLiveData = MutableLiveData<Resource<PriceResponse?>>()

    val newsLocalLiveData = MutableLiveData<List<NewsEntity?>>()
    val priceLocalLiveData = MutableLiveData<List<PriceResponseEntity?>>()


    fun login(username: String, password: String) =
        authRepository.login(username, password)

    fun register(body: RegisterRequestBody) =
        authRepository.register(body)

    fun saveNews(newsEntity: NewsEntity) {
        viewModelScope.launch {
            sawitRepository.insertNews(newsEntity)
        }
    }

    fun changePassword(old_password: String, new_password: String) =
        authRepository.changePassword(old_password, new_password)

    fun updateProfilePicture(file: File) =
        authRepository.changeProfilePicture(file)


    fun saveAuthInfo(authEntity: AuthEntity) =
        viewModelScope.launch {
            authRepository.localSaveAuthInfo(
                authEntity
            )
        }

    fun updatePhoto(photo: String) =
        viewModelScope.launch {
            authRepository.updatePhoto(photo)
        }

    fun getNews() = viewModelScope.launch {
        try {
            val news = sawitRepository.getNews()
            if (news.isSuccessful) {
                newsLiveData.postValue(Resource.Success(news.body()))
            } else {
                newsLiveData.postValue(Resource.Error("Terjadi Kesalahan"))
            }
        } catch (e: Exception) {
            newsLiveData.postValue(Resource.Error(e.message.toString()))
        }
    }

    fun getNewsLocally() {
        viewModelScope.launch {
            val newsLocal = sawitRepository.getNewsLocally()
            newsLocalLiveData.postValue(newsLocal)
        }
    }


    fun getPriceLocally() {
        viewModelScope.launch {
            val data = sawitRepository.getPricesLocally()
            priceLocalLiveData.postValue(data)
        }
    }

    fun getPrices(saveLocally: Boolean = false) = viewModelScope.launch {
        try {
            val data = sawitRepository.getPrices()
            if (data.isSuccessful) {
                pricesLiveData.postValue(Resource.Success(data.body()))

                if (saveLocally) {
                    data.body()?.forEachIndexed { index, priceResponseEntity ->
                        sawitRepository.insertPrice(
                            priceResponseEntity
                        )
                    }
                }

                priceLocalLiveData.postValue(sawitRepository.getPricesLocally())

            } else {
                pricesLiveData.postValue(Resource.Error("Terjadi Kesalahan"))
            }
        } catch (e: Exception) {
            pricesLiveData.postValue(Resource.Error(e.message.toString()))
        }
    }

    fun saveNewsFromResponse(newsResponse: NewsResponse.NewsResponseItem) {
        saveNews(
            NewsEntity(
                newsResponse.id,
                newsResponse.title,
                newsResponse.author,
                newsResponse.content,
                newsResponse.photo,
                newsResponse.createdAt,
                newsResponse.updatedAt
            )
        )
    }

    fun getProfileLocally() {
        viewModelScope.launch {
            val ld = authRepository.getUserInfoLocally()
            if (ld.isNotEmpty())
                localProfileLD.postValue(ld[0])
        }
    }

}