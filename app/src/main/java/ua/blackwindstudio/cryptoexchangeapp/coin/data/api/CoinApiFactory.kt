package ua.blackwindstudio.cryptoexchangeapp.coin.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object CoinApiFactory {

    private const val BASE_URL = "https://min-api.cryptocompare.com/data/"
    const val BASE_IMAGE_URL = "https://www.cryptocompare.com/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: CoinApi = retrofit.create(CoinApi::class.java)
}