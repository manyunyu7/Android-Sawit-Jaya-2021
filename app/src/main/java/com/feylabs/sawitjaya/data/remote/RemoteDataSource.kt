package com.feylabs.sawitjaya.data.remote

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.feylabs.sawitjaya.data.remote.response.LoginResponse
import com.feylabs.sawitjaya.service.MainEndpoint
import com.feylabs.sawitjaya.service.Resource
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback as retrocallbak

class RemoteDataSource(
    private val api: MainEndpoint,
) {


    /*
    Login
    @param username,password
     */
    fun login(
        username: String,
        password: String
    ): MutableLiveData<Resource<LoginResponse?>> {

        val returnValue = MutableLiveData<Resource<LoginResponse?>>()

        api.login(
            username,
            password
        ).enqueue(object : retrocallbak<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>, response: Response<LoginResponse>
            ) {
                if (response.body()?.status_code == 1) {
                    returnValue.postValue(Resource.Success(response.body(), ""))
                } else {
                    returnValue.postValue(Resource.Error(response.body()?.message.toString()))
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                returnValue.postValue(Resource.Error("Terjadi Kesalahan"))
            }
        })

        return returnValue

    }

}