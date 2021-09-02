package com.feylabs.sawitjaya.data

import androidx.lifecycle.MutableLiveData as MLD
import com.feylabs.sawitjaya.data.local.LocalDataSource
import com.feylabs.sawitjaya.data.local.room.entity.AuthEntity
import com.feylabs.sawitjaya.data.remote.RemoteDataSource as remote
import com.feylabs.sawitjaya.data.remote.request.RegisterRequestBody
import com.feylabs.sawitjaya.data.remote.response.ChangePasswordResponse
import com.feylabs.sawitjaya.data.remote.response.UserUpdateProfileResponse
import java.io.File
import com.feylabs.sawitjaya.utils.service.Resource as Res

class AuthRepository(
    private val remoteDs: remote,
    private val localDs: LocalDataSource,
) {

    fun saveNewJWTToken(token: String) {
        localDs.saveNewJWTToken(token)
    }

    fun login(username: String, password: String) =
        remoteDs.login(username, password)

    fun register(body: RegisterRequestBody): MLD<Res<String?>> {
        val retVal = MLD<Res<String?>>()
        remoteDs.register(body, object : remote.CallbackRegisterProfile {
            override fun value(response: Res<String?>) {
                retVal.postValue(response)
            }
        })
        return retVal
    }

    fun updatePhoto(photo:String){
        localDs.updatePhoto(photo)
    }

    fun changePassword(old_password: String, new_password: String)
            : MLD<Res<ChangePasswordResponse?>> {
        val retVal = MLD<Res<ChangePasswordResponse?>>()
        remoteDs.changePassword(old_password, new_password,
            callback = object : remote.CallbackChangePasswordProfile {
                override fun value(response: Res<ChangePasswordResponse?>) {
                    retVal.postValue(response)
                }
            })
        return retVal
    }

    fun changeProfilePicture(file: File): MLD<Res<String?>> {
        val retVal = MLD<Res<String?>>()
        remoteDs.changeProfiePicture(file, object : remote.CallbackChangeProfilePicture {
            override fun value(response: Res<String?>) {
                retVal.postValue(response)
            }
        })
        return retVal
    }

    suspend fun localSaveAuthInfo(authEntity: AuthEntity) {
        localDs.saveAuthInfo(authEntity)
    }

    suspend fun getUserInfoLocally() =
        localDs.getUserInfo()

    fun updateUserData(
        name: String,
        email: String,
        contact: String,
        role: String
    ): MLD<Res<UserUpdateProfileResponse?>> {

        val retVal = MLD<Res<UserUpdateProfileResponse?>>()

        remoteDs.updateProfile(
            name = name,
            email = email,
            contact = contact,
            role = role,
            callback = object : remote.CallbackUpdateProfile {
                override fun value(response: Res<UserUpdateProfileResponse?>) {
                    retVal.value = response
                }
            }
        )
        return retVal
    }

}