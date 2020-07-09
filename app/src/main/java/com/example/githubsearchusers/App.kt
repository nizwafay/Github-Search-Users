package com.example.githubsearchusers

import android.app.Application
import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App: Application() {
    companion object {
        lateinit var context: Context
            private set
    }

    override fun onCreate() {
        super.onCreate()
        context = this
        // initialization of Dependency Injection library to allow the use of application context
        startKoin { androidContext(this@App) }
    }
}