package com.feylabs.sawitjaya.ui.auth.viewmodel

import androidx.lifecycle.ViewModel
import com.feylabs.sawitjaya.data.AuthRepository

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {

    fun login(username: String, password: String) =
        repository.login(username, password)

}