package com.feylabs.sawitjaya.injection

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.feylabs.sawitjaya.data.local.preference.MyPreference
import com.feylabs.sawitjaya.data.remote.response.RefreshTokenResponse
import com.feylabs.sawitjaya.data.remote.service.ApiClient
import com.feylabs.sawitjaya.ui.auth.ContainerAuthActivity
import okhttp3.*
import retrofit2.Call
import retrofit2.Callback
import timber.log.Timber

class TokenAuthenticator(val pref: MyPreference, val context: Context) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        // This is a synchronous call
        val updatedToken = getUpdatedToken()
        return response.request.newBuilder()
            .header("Authorization", "233")
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
}