package ua.blackwindstudio.cryptoexchangeapp.di

import androidx.work.WorkManager
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ua.blackwindstudio.cryptoexchangeapp.coin.data.CoinRepository
import ua.blackwindstudio.cryptoexchangeapp.coin.data.CoinRepositoryImpl
import ua.blackwindstudio.cryptoexchangeapp.coin.data.db.CoinDatabase
import javax.inject.Singleton

@Module
class RepositoryModule {
    @OptIn(ExperimentalCoroutinesApi::class)
    @Provides
    @Singleton
    fun provideCoinRepository(db: CoinDatabase, manager: WorkManager): CoinRepository =
        CoinRepositoryImpl(db, manager)
}

