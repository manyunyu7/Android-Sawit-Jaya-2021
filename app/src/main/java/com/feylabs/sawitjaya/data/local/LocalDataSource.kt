package com.feylabs.sawitjaya.data.local

import com.feylabs.sawitjaya.data.local.preference.MyPreference
import com.feylabs.sawitjaya.data.local.room.MyRoomDatabase
import com.feylabs.sawitjaya.data.local.room.entity.AuthEntity
import com.feylabs.sawitjaya.data.local.room.entity.NewsEntity
import com.feylabs.sawitjaya.data.local.room.entity.PriceResponseEntity

class LocalDataSource(
    private val db: MyRoomDatabase,
) {


    fun saveAuthInfo(userBaseInfoLocalEntity: AuthEntity) {
        db.authDao().clear();
        db.authDao().insertOrUpdate(userBaseInfoLocalEntity)
    }

    suspend fun getUserID() = db.authDao().getUserID()

    fun saveNews(newsEntity: NewsEntity) {
        db.newsDao().insert(newsEntity)
    }

    fun savePrices(entity: PriceResponseEntity) {
        db.pricesDao().insert(entity)
    }

    fun getNews() = db.newsDao().findAll()
    fun clearNews() = db.newsDao().clear()

    suspend fun getPrice() = db.pricesDao().findAll()

    fun updatePhoto(photo: String) {
        db.authDao().updatePhoto(photo)
    }

    fun getUserInfo() = db.authDao().findAll()


}