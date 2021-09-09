package com.feylabs.sawitjaya.injection

import androidx.room.Room
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.feylabs.sawitjaya.data.AuthRepository
import com.feylabs.sawitjaya.data.SawitRepository
import com.feylabs.sawitjaya.data.local.LocalDataSource
import com.feylabs.sawitjaya.data.local.room.MyRoomDatabase
import com.feylabs.sawitjaya.data.remote.RemoteDataSource
import com.feylabs.sawitjaya.utils.service.ApiService
import com.feylabs.sawitjaya.utils.service.MainEndpoint
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


val databaseModule = module {
    factory { get<MyRoomDatabase>().authDao() }
    factory { get<MyRoomDatabase>().newsDao() }
    factory { get<MyRoomDatabase>().pricesDao() }
    single {
        Room.databaseBuilder(
            androidContext(),
            MyRoomDatabase::class.java, "MyDatabase.db"
        ).fallbackToDestructiveMigration().build()
    }
}

val networkModule = module {
    single {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addInterceptor(
                ChuckerInterceptor.Builder(androidContext())
                    .collector(ChuckerCollector(androidContext()))
                    .maxContentLength(250000L)
                    .redactHeaders(emptySet())
                    .alwaysReadResponseBody(true)
                    .build()
            )
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .build()
    }
    single {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.1.161:3202/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
        retrofit.create(MainEndpoint::class.java)
    }
}

val repositoryModule = module {
    single { RemoteDataSource(get(), get()) }
    single { LocalDataSource(get()) }
    single { SawitRepository(get(), get()) }
    single { AuthRepository(get(), get()) }
}
