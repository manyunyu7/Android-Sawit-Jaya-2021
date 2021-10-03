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


class HttpCustomInterceptor(
    private val mySharedPreferences: MyPreference,
    private val context: Context,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val req = chain.request()
        val response = chain.proceed(req)

        when (response.code) {
            401 -> {
                mySharedPreferences.clearPreferences()
                if (context is Activity) {
                    context.finish()
                    Toast.makeText(
                        context,
                        "Sesi Anda Telah Habis, Silakan Login Kembali",
                        Toast.LENGTH_LONG
                    ).show()
                }
                val intent = Intent(context, ContainerAuthActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK;
                context.startActivity(Intent(context, ContainerAuthActivity::class.java))
            }
        }
        return response
    }


}