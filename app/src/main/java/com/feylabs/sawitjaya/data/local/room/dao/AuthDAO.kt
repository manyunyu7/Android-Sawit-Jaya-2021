package com.feylabs.sawitjaya.data.local.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.feylabs.sawitjaya.data.local.room.entity.AuthEntity

@Dao
interface AuthDAO {
    @Query("SELECT * FROM tb_user_base_info WHERE user_id=:id")
    fun findById(id: Int): AuthEntity

    @Query("SELECT * FROM tb_user_base_info ORDER BY user_id ASC")
    fun findAll(): List<AuthEntity>

    @Query("SELECT * FROM tb_user_base_info ORDER BY user_id ASC")
    fun findAllData(): List<AuthEntity?>?

    @Query("SELECT * FROM tb_user_base_info WHERE user_id IN (:ids) ORDER BY user_id ASC")
    fun findByIdIn(ids: List<Int?>?): LiveData<List<AuthEntity?>?>?

}