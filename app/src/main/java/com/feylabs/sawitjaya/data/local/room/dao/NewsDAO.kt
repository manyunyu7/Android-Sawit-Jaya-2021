package com.feylabs.sawitjaya.data.local.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.feylabs.sawitjaya.data.local.room.entity.AuthEntity
import com.feylabs.sawitjaya.data.local.room.entity.NewsEntity

@Dao
interface NewsDAO {
    @Query("SELECT * FROM news WHERE id=:id")
    fun findById(id: Int): NewsEntity

    @Query("SELECT * FROM news ORDER BY id ASC")
    fun findAll(): List<AuthEntity>

    @Query("SELECT * FROM news ORDER BY id ASC")
    fun findAllData(): List<NewsEntity?>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(news: NewsEntity?): Long


    @Query("DELETE FROM news where  created_at <> 's' ")
    fun clear()

    @Query("SELECT * FROM news WHERE id IN (:ids) ORDER BY created_at ASC")
    fun findByIdIn(ids: List<Int?>?): LiveData<List<AuthEntity?>?>?

}