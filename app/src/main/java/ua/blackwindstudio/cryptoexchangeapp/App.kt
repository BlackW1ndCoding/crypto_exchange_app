package ua.blackwindstudio.cryptoexchangeapp

import android.app.Application
import android.content.Context
import androidx.work.Configuration
import androidx.work.WorkManager
import ua.blackwindstudio.cryptoexchangeapp.coin.data.network.workers.BaseWorkerFactory
import ua.blackwindstudio.cryptoexchangeapp.di.AppComponent
import ua.blackwindstudio.cryptoexchangeapp.di.DaggerAppComponent
import javax.inject.Inject

class App: Application() {

    lateinit var appComponent: AppComponent

    @Inject
    lateinit var baseWorkerFactory: BaseWorkerFactory

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this

        appComponent = DaggerAppComponent.builder()
            .context(this)
            .build()

        appComponent.inject(this)

        val workManagerConfig = Configuration.Builder()
            .setWorkerFactory(baseWorkerFactory)
            .build()
        WorkManager.initialize(this, workManagerConfig)
    }

    companion object {
        @Volatile
        var INSTANCE: Application? = null
    }
}

val Context.appComponent: AppComponent
    get() =
        when (this) {
            is Application -> (this as App).appComponent
            else -> this.applicationContext.appComponent
        }

