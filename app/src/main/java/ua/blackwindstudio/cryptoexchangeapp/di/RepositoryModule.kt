package ua.blackwindstudio.cryptoexchangeapp.di

import dagger.Binds
import dagger.Module
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ua.blackwindstudio.cryptoexchangeapp.coin.data.CoinRepository
import ua.blackwindstudio.cryptoexchangeapp.coin.data.CoinRepositoryImpl
import javax.inject.Singleton

@Module
interface RepositoryModule {
    @OptIn(ExperimentalCoroutinesApi::class)
    @Binds
    @Singleton
    fun bindCoinRepository(impl: CoinRepositoryImpl): CoinRepository
}

