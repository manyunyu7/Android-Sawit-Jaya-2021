package com.feylabs.sawitjaya.data.local.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.feylabs.sawitjaya.data.local.room.entity.AuthEntity
import com.feylabs.sawitjaya.data.local.room.entity.NewsEntity
import com.feylabs.sawitjaya.data.local.room.entity.PriceResponseEntity

@Dao
interface PriceDAO {
    @Query("SELECT * FROM price WHERE id=:id")
    fun findById(id: Int): NewsEntity

    @Query("SELECT * FROM price ORDER BY id DESC")
    fun findAll(): List<PriceResponseEntity>

    @Query("SELECT * FROM price ORDER BY id ASC")
    fun findAllData(): List<PriceResponseEntity?>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(news: PriceResponseEntity?): Long

    @Query("DELETE FROM price where  createdAt <> 's' ")
    fun clear()

    @Query("SELECT * FROM price WHERE id IN (:ids) ORDER BY createdAt ASC")
    fun findByIdIn(ids: List<Int?>?): LiveData<List<PriceResponseEntity?>?>?

}