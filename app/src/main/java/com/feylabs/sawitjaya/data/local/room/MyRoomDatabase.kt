package com.feylabs.sawitjaya.data.local.room

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteOpenHelper
import com.feylabs.sawitjaya.data.local.room.dao.AuthDAO
import com.feylabs.sawitjaya.data.local.room.dao.NewsDAO
import com.feylabs.sawitjaya.data.local.room.dao.PriceDAO
import com.feylabs.sawitjaya.data.local.room.entity.AuthEntity
import com.feylabs.sawitjaya.data.local.room.entity.NewsEntity
import com.feylabs.sawitjaya.data.local.room.entity.PriceResponseEntity

@Database(
    entities = [
        AuthEntity::class,
        NewsEntity::class,
        PriceResponseEntity::class
    ],
    version = 5
)
abstract class MyRoomDatabase : RoomDatabase() {

    abstract fun authDao(): AuthDAO
    abstract fun newsDao(): NewsDAO
    abstract fun pricesDao(): PriceDAO

    companion object {
        const val DB_NAME = "MyDatabase.db"

        @Volatile
        private var instance: MyRoomDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                MyRoomDatabase::class.java, DB_NAME
            ).allowMainThreadQueries()
                .build()

    }
}