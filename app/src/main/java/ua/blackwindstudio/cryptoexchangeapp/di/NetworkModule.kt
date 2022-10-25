package ua.blackwindstudio.cryptoexchangeapp.di

import android.content.Context
import androidx.work.WorkManager
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ua.blackwindstudio.cryptoexchangeapp.coin.data.network.CoinApi
import javax.inject.Singleton

@Module
class NetworkModule {
    @Provides
    @Singleton
    fun provideCoinApi(): CoinApi {
        val retrofit = Retrofit.Builder()
            .baseUrl(CoinApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(CoinApi::class.java)
    }


    @Provides
    fun provideWorkManager(context: Context): WorkManager = WorkManager.getInstance(context)
}