package com.feylabs.sawitjaya.injection

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.feylabs.sawitjaya.data.local.preference.MyPreference
import com.feylabs.sawitjaya.ui.auth.ContainerAuthActivity
import okhttp3.Interceptor
import okhttp3.Response
import android.app.Activity
import android.widget.Toast
import com.feylabs.sawitjaya.data.remote.response.RefreshTokenResponse
import com.feylabs.sawitjaya.data.remote.service.ApiClient
import retrofit2.Call
import retrofit2.Callback
import timber.log.Timber


class HttpCustomInterceptor(
    private val mySharedPreferences: MyPreference,
    private val context: Context,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val req = chain.request()
        val response = chain.proceed(req)
        when (response.code) {
            401 -> {
//                updateToken()
//                mySharedPreferences.clearPreferences()
//                if (context is Activity) {
//                    context.finish()
//                }
//                val intent = Intent(context, ContainerAuthActivity::class.java)
//                intent.putExtra("message","Sesi Anda Telah Habis, Silakan Login Kembali")
//                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK;
//                context.startActivity(intent)
            }
        }
        return response
    }

    private fun updateToken() {

        var authTokenResponse = ""
        val req = ApiClient.getClient(context).refreshToken(mySharedPreferences.getToken())

        req.enqueue(object : Callback<RefreshTokenResponse?> {
            override fun onResponse(
                call: Call<RefreshTokenResponse?>,
                response: retrofit2.Response<RefreshTokenResponse?>
            ) {
                authTokenResponse = response.body()?.accessToken.toString()
            }

            override fun onFailure(call: Call<RefreshTokenResponse?>, t: Throwable) {
                Timber.d("NRY authenticator error : ${t.toString()}")
                MyPreference(context).clearPreferences()
                if (context is Activity) {
                    context.finish()
                }
                val intent = Intent(context, ContainerAuthActivity::class.java)
                intent.putExtra("message", "Sesi Anda Telah Habis, Silakan Login Kembali")
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK;
                context.startActivity(intent)
            }


        })


        MyPreference(context).saveTokenWithTemplate(authTokenResponse.toString())
        val newToken = MyPreference(context).getToken().toString()
        Timber.d("updated_token")
    }


}