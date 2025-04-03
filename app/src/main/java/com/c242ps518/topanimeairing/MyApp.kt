package com.c242ps518.topanimeairing

import android.app.Application
import com.c242ps518.core.di.databaseModule
import com.c242ps518.core.di.networkModule
import com.c242ps518.core.di.repositoryModule
import com.c242ps518.topanimeairing.di.useCaseModule
import com.c242ps518.topanimeairing.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.logger.Level

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@MyApp)
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