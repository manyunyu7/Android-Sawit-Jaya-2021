package com.feylabs.sawitjaya

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.feylabs.sawitjaya.data.AuthRepository
import com.feylabs.sawitjaya.ui.auth.viewmodel.AuthViewModel

class ViewModelFactory constructor(
    private val repoAuth: AuthRepository
) :

    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> {
                AuthViewModel(repoAuth) as T
            }

            else -> throw Throwable("Unknown ViewModel class: " + modelClass.name)
        }

    }
}