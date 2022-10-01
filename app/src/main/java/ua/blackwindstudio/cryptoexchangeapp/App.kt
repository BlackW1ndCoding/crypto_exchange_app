package ua.blackwindstudio.cryptoexchangeapp

import android.app.Application

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
    }

    companion object {
        @Volatile
        var INSTANCE: Application? = null
    }
}