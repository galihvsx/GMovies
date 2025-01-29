package com.gtools.gmovies

import android.app.Application
import com.gtools.gmovies.core.di.databaseModule
import com.gtools.gmovies.core.di.networkModule
import com.gtools.gmovies.core.di.repositoryModule
import com.gtools.gmovies.di.useCaseModule
import com.gtools.gmovies.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@MyApplication)
            modules(
                listOf(
                    databaseModule,
                    networkModule,
                    repositoryModule,
                    useCaseModule,
                    viewModelModule
                )
            )
        }
    }
}