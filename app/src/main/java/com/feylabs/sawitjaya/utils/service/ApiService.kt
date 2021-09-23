package com.feylabs.sawitjaya.utils.service


import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.feylabs.sawitjaya.injection.ServiceLocator
import com.google.gson.GsonBuilder
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import okhttp3.OkHttpClient


object ApiService {
//    const val baseURL = "http://192.168.1.161:3202/api/"
//    const val baseURL = "http://sawit-jaya.feylabs.my.id/api/"

    fun getClient(context: Context): MainEndpoint {


        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(
                ChuckerInterceptor.Builder(context)
                    .collector(ChuckerCollector(context))
                    .maxContentLength(250000L)
                    .redactHeaders(emptySet())
                    .alwaysReadResponseBody(true)
                    .build()
            )
            .build()

        val gson = GsonBuilder().serializeNulls().disableHtmlEscaping().create()

        val retrofitz = Retrofit.Builder()
            .baseUrl(ServiceLocator.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()

        return retrofitz.create(MainEndpoint::class.java)

    }

}