package com.feylabs.sawitjaya.ui.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feylabs.sawitjaya.data.AuthRepository
import com.feylabs.sawitjaya.data.local.room.entity.AuthEntity
import kotlinx.coroutines.launch
import java.io.File

class SettingsViewModel(
    val repoAuth: AuthRepository
) : ViewModel() {

    val localProfileLD = MutableLiveData<AuthEntity>()

    fun getProfileLocally() {
        viewModelScope.launch {
            val ld = repoAuth.getUserInfoLocally()
            localProfileLD.postValue(ld[0])
        }
    }

    fun updateProfile(name: String, email: String, contact: String, mRole: String) =
        repoAuth.updateUserData(name = name, email = email, contact = contact, role = mRole)

    fun updateProfilePicture(file: File){
        repoAuth.changeProfilePicture(file)
    }


}