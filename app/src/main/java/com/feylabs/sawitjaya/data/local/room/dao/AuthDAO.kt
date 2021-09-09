package com.feylabs.sawitjaya.data.local.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.feylabs.sawitjaya.data.local.room.entity.AuthEntity

@Dao
interface AuthDAO {
    @Query("SELECT * FROM tb_user_base_info WHERE user_id=:id")
    fun findById(id: Int): AuthEntity

    @Query("SELECT user_id FROM tb_user_base_info LIMIT 1")
    fun getUserID(): Int

    @Query("SELECT * FROM tb_user_base_info ORDER BY user_id ASC")
    fun findAll(): List<AuthEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdate(authEntity: AuthEntity?): Long

    @Query("SELECT * FROM tb_user_base_info ORDER BY user_id ASC")
    fun findAllData(): List<AuthEntity?>?

    @Query("UPDATE TB_USER_BASE_INFO SET photo=:newPhoto where user_id <>0")
    fun updatePhoto(newPhoto: String)

    @Query("DELETE FROM tb_user_base_info where  user_id <> 0")
    fun clear()

    @Query("SELECT * FROM tb_user_base_info WHERE user_id IN (:ids) ORDER BY user_id ASC")
    fun findByIdIn(ids: List<Int?>?): LiveData<List<AuthEntity?>?>?

}