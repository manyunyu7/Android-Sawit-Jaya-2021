package com.feylabs.sawitjaya.injection

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.feylabs.sawitjaya.data.local.preference.MyPreference
import okhttp3.Interceptor
import okhttp3.Response
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.StringRequestListener
import com.feylabs.sawitjaya.data.remote.response.LoginResponse
import com.feylabs.sawitjaya.data.remote.response.RefreshTokenResponse
import com.feylabs.sawitjaya.injection.ServiceLocator.BASE_URL
import com.feylabs.sawitjaya.ui.auth.ContainerAuthActivity
import com.google.gson.Gson
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
            200->{
                Timber.d("intercept-manyunyu7 200 lanjutt")
            }
            401->{
               logout()
            }
            2201 -> {
                val newToken = updateToken(chain)
                Timber.d("updateTOKEN ${newToken}")
                Timber.d("intercept-manyunyu7 401 refresh with $newToken")
                Timber.d("newTOKEN MANYUNYU7 ${newToken}")
                val newRequest = chain.request().newBuilder()
                    .addHeader("Authorization", newToken)
                    .build()
                chain.proceed(newRequest)
            }
        }
        return response
    }

    private fun updateToken(chain: Interceptor.Chain): String {

        var authTokenResponse = ""

        Timber.d("NRY requesting start req new token with ${mySharedPreferences.getToken()} ")

        AndroidNetworking.post(BASE_URL + "auth/refresh")
            .addHeaders("Authorization", mySharedPreferences.getToken())
            .build()
            .getAsString(object : StringRequestListener {
                override fun onResponse(response: String?) {
                    val json = Gson().fromJson(response, RefreshTokenResponse::class.java)
                    val newToken = json.accessToken
                    authTokenResponse = newToken
                    Timber.d("nry fan response ${response}")
                    MyPreference(context).saveTokenWithTemplate(newToken)
                    MyPreference(context).getToken().toString()
                }

                override fun onError(anError: ANError?) {
                    Timber.d("nry fan response ${anError}")
                    Timber.d("nry fan response er body ${anError?.errorBody}")
                    Timber.d("nry fan response er code${anError?.errorCode}")
                    Timber.d("nry fan response er det${anError?.errorDetail}")
                }

            })

        return authTokenResponse
    }

    private fun updateToken(): String {

        var authTokenResponse = ""

        Timber.d("NRY requesting start req new token with ${MyPreference(context).getToken()} ")


        AndroidNetworking.post(ServiceLocator.BASE_URL + "auth/login")
            .addHeaders("Authorization", MyPreference(context).getToken())
            .addBodyParameter("email", MyPreference(context).getUserEmail())
            .addBodyParameter("password", MyPreference(context).getUserPassword())
            .build()
            .getAsString(object : StringRequestListener {
                override fun onResponse(response: String?) {
                    val json = Gson().fromJson(response, LoginResponse::class.java)
                    val newToken = json.access_token

                    authTokenResponse = newToken
                    Timber.d("nry fan response ${response}")
                    MyPreference(context).saveTokenWithTemplate(newToken)
                    MyPreference(context).getToken().toString()
                }

                override fun onError(anError: ANError?) {
                    Timber.d("nry fan response ${anError}")
                    Timber.d("nry fan response er body ${anError?.errorBody}")
                    Timber.d("nry fan response er code${anError?.errorCode}")
                    Timber.d("nry fan response er det${anError?.errorDetail}")
                    logout()
                }

            })

        return authTokenResponse

//        req.enqueue(object : Callback<RefreshTokenResponse?> {
//            override fun onResponse(
//                call: Call<RefreshTokenResponse?>,
//                response: retrofit2.Response<RefreshTokenResponse?>
//            ) {
//                Timber.d("NRY requesting new token")
//                authTokenResponse = response.body()?.accessToken.toString()
//                Timber.d("NRY requesting new token result body ${response.body()}")
//                Timber.d("NRY requesting new token result $authTokenResponse")
//            }
//
//            override fun onFailure(call: Call<RefreshTokenResponse?>, t: Throwable) {
//                Timber.d("NRY authenticator error : ${t.toString()}")
//                Timber.d("NRY requesting new token result ${t.toString()}")
//            }
//
//
//        })

//        Timber.d("NRY requesting end req new token ")


//        Timber.d("updated_token")
    }

    private fun logout() {
        MyPreference(context).clearPreferences()
        if (context is Activity) {
            context.finish()
        }
        val intent = Intent(context, ContainerAuthActivity::class.java)
        intent.putExtra("message", "Sesi Anda Telah Habis, Silakan Login Kembali")
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK;
        context.startActivity(intent)
    }


}