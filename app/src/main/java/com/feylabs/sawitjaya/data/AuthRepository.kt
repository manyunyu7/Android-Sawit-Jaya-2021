package com.feylabs.sawitjaya.data

import com.feylabs.sawitjaya.data.remote.RemoteDataSource

class AuthRepository(
    private val rds: RemoteDataSource,
) {

    suspend fun login(username: String, password: String) =
        rds.login(username,password)


}