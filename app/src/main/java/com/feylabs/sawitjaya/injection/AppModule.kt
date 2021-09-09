package com.feylabs.sawitjaya.injection

import androidx.room.Room
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.feylabs.sawitjaya.data.AuthRepository
import com.feylabs.sawitjaya.data.SawitRepository
import com.feylabs.sawitjaya.data.local.LocalDataSource
import com.feylabs.sawitjaya.data.local.room.MyRoomDatabase
import com.feylabs.sawitjaya.data.remote.RemoteDataSource
import com.feylabs.sawitjaya.ui.auth.viewmodel.AuthViewModel
import com.feylabs.sawitjaya.ui.profile.SettingsViewModel
import com.feylabs.sawitjaya.ui.rs.RsDetailViewModel
import com.feylabs.sawitjaya.ui.staff.home.StaffHomeViewModel
import com.feylabs.sawitjaya.ui.user_history_tbs.HistoryViewModel
import com.feylabs.sawitjaya.utils.service.ApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


val viewModelModule = module {
    viewModel{AuthViewModel(get(),get())}
    viewModel{RsDetailViewModel(get())}
    viewModel{SettingsViewModel(get())}
    viewModel{StaffHomeViewModel()}
    viewModel{HistoryViewModel(get())}
}

