package com.feylabs.sawitjaya.injection

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.StringRequestListener
import com.feylabs.sawitjaya.data.local.preference.MyPreference
import com.feylabs.sawitjaya.data.remote.response.RefreshTokenResponse
import com.feylabs.sawitjaya.data.remote.service.ApiClient
import com.feylabs.sawitjaya.ui.auth.ContainerAuthActivity
import com.google.gson.Gson
import okhttp3.*
import retrofit2.Call
import retrofit2.Callback
import timber.log.Timber

class TokenAuthenticator(val pref: MyPreference, val context: Context) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        // This is a synchronous call
        val updatedToken = updateToken()
        return response.request.newBuilder()
            .header("Authorization", updatedToken)
            .build()
    }

    private fun getUpdatedToken(): String {

        var authTokenResponse = ""
        val req = ApiClient.getClient(context).refreshToken(pref.getToken())

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
        return newToken
    }

    private fun updateToken(): String {

        var authTokenResponse = ""

        Timber.d("NRY requesting start req new token with ${MyPreference(context).getToken()} ")

        AndroidNetworking.post(ServiceLocator.BASE_URL + "auth/refresh")
            .addHeaders("Authorization", MyPreference(context).getToken())
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
//                MyPreference(context).clearPreferences()
//                if (context is Activity) {
//                    context.finish()
//                }
//                val intent = Intent(context, ContainerAuthActivity::class.java)
//                intent.putExtra("message", "Sesi Anda Telah Habis, Silakan Login Kembali")
//                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK;
//                context.startActivity(intent)
//            }
//
//
//        })

//        Timber.d("NRY requesting end req new token ")


//        Timber.d("updated_token")
    }

}