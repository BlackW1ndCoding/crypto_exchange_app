package ua.blackwindstudio.cryptoexchangeapp.coin.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object CoinApiFactory {

    private const val BASE_URL = "https://min-api.cryptocompare.com/data/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: CoinApi = retrofit.create(CoinApi::class.java)
}