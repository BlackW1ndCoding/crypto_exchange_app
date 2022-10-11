package ua.blackwindstudio.cryptoexchangeapp

import android.app.Application

class App: Application() {
//
//    init {
//        StrictMode.enableDefaults()
//    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
    }

    companion object {
        @Volatile
        var INSTANCE: Application? = null
    }
}