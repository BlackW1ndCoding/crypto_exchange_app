package ua.blackwindstudio.cryptoexchangeapp.coin.data

import retrofit2.http.GET

interface CoinApi {
    @GET()
    fun getTopCoinsInfoByCurrency(){

    }

    @GET()
    fun getFullPriceListByCurrency(){

    }
}