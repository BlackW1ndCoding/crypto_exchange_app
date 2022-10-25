package ua.blackwindstudio.cryptoexchangeapp.coin.data.db.model

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import ua.blackwindstudio.cryptoexchangeapp.coin.data.db.CoinDatabase
import javax.inject.Singleton

@Module
class CoinDBModule {
    @Singleton
    @Provides
    fun provideDB(context: Context): CoinDatabase =
        Room.databaseBuilder(
            context,
            CoinDatabase::class.java,
            CoinDatabase.DB_NAME
        ).build()
}