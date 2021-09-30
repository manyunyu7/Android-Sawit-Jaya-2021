package com.feylabs.sawitjaya.injection

import com.feylabs.sawitjaya.ui.auth.viewmodel.AuthViewModel
import com.feylabs.sawitjaya.ui.mnotification.MNotificationViewModel
import com.feylabs.sawitjaya.ui.news.NewsViewModel
import com.feylabs.sawitjaya.ui.profile.SettingsViewModel
import com.feylabs.sawitjaya.ui.rs.request.RsDetailViewModel
import com.feylabs.sawitjaya.ui.staff.home.StaffHomeViewModel
import com.feylabs.sawitjaya.ui.user_history_tbs.HistoryViewModel
import com.feylabs.sawitjaya.ui.user_history_tbs.detail.DetailHistoryViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module


val viewModelModule = module {
    viewModel { AuthViewModel(get(), get()) }
    viewModel { RsDetailViewModel(get()) }
    viewModel { SettingsViewModel(get()) }
    viewModel { StaffHomeViewModel() }
    viewModel { HistoryViewModel(get()) }
    viewModel { DetailHistoryViewModel(get()) }
    viewModel { NewsViewModel(get()) }
    viewModel { MNotificationViewModel(get()) }
}

