package com.feylabs.sawitjaya

import android.app.Application
import com.feylabs.sawitjaya.injection.databaseModule
import com.feylabs.sawitjaya.injection.networkModule
import com.feylabs.sawitjaya.injection.repositoryModule
import com.feylabs.sawitjaya.injection.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

open class Application : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())

        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@Application)
            modules(listOf(
                databaseModule,
                networkModule,
                repositoryModule,
                viewModelModule
            ))
        }
    }

}