package com.feylabs.sawitjaya.data.local

import com.feylabs.sawitjaya.data.local.preference.MyPreference
import com.feylabs.sawitjaya.data.local.room.MyRoomDatabase
import com.feylabs.sawitjaya.data.local.room.entity.AuthEntity
import com.feylabs.sawitjaya.data.local.room.entity.NewsEntity
import com.feylabs.sawitjaya.data.local.room.entity.PriceResponseEntity

class LocalDataSource(
    private val db: MyRoomDatabase,
    private val pref: MyPreference
) {


    fun saveAuthInfo(userBaseInfoLocalEntity: AuthEntity) {
        db.authDao().clear();
        db.authDao().insertOrUpdate(userBaseInfoLocalEntity)
    }

    fun saveNews(newsEntity: NewsEntity) {
        db.newsDao().insert(newsEntity)
    }

    fun savePrices(entity: PriceResponseEntity) {
        db.pricesDao().insert(entity)
    }

    fun getNews() = db.newsDao().findAll()
    suspend fun getPrice() = db.pricesDao().findAll()

    fun updatePhoto(photo: String) {
        db.authDao().updatePhoto(photo)
    }

    fun saveNewJWTToken(token: String) {
        pref.save("TOKEN", token)
    }

    fun getUserInfo() = db.authDao().findAll()


}