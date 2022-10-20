package ua.blackwindstudio.cryptoexchangeapp.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import ua.blackwindstudio.cryptoexchangeapp.App
import ua.blackwindstudio.cryptoexchangeapp.coin.data.db.CoinDatabase
import ua.blackwindstudio.cryptoexchangeapp.coin.data.db.model.CoinDBModule
import ua.blackwindstudio.cryptoexchangeapp.coin.ui.coin_list.CoinListFragment
import javax.inject.Singleton

@Component(
    modules = [
        RepositoryModule::class,
        CoinDBModule::class,
        NetworkModule::class
    ]
)
@Singleton
interface AppComponent {

    fun inject(app: App)

    fun inject(fragment: CoinListFragment)

    fun inject(db: CoinDatabase)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun context(context: Context): Builder

        fun build(): AppComponent
    }
}