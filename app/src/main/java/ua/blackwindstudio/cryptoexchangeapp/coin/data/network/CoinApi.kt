package ua.blackwindstudio.cryptoexchangeapp.coin.data.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import ua.blackwindstudio.cryptoexchangeapp.coin.data.network.model.CoinsInfoContainerDto
import ua.blackwindstudio.cryptoexchangeapp.coin.data.network.model.CoinNamesListDto

interface CoinApi {
    @GET("top/totalvolfull")
    suspend fun getTopCoinsInfoByCurrency(
        @Query(QUERY_PARAM_API_KEY) apiKey: String = "",
        @Query(QUERY_PARAM_LIMIT) limit: Int = 10,
        @Query(QUERY_PARAM_TO_SYMBOL) tSym: String = DEFAULT_CURRENCY
    ): Response<CoinNamesListDto>

    @GET("pricemultifull")
    suspend fun getFullPriceListByCurrency(
//        @Query(QUERY_PARAM_API_KEY) apiKey: String = "",
        @Query(QUERY_PARAM_FROM_SYMBOL) fSyms: String,
        @Query(QUERY_PARAM_TO_SYMBOLS) tSyms: String = DEFAULT_CURRENCY
    ): Response<CoinsInfoContainerDto>

    companion object {
        private const val QUERY_PARAM_API_KEY = "api_key"
        private const val QUERY_PARAM_LIMIT = "limit"
        private const val QUERY_PARAM_FROM_SYMBOL = "fsyms"
        private const val QUERY_PARAM_TO_SYMBOL = "tsym"
        private const val QUERY_PARAM_TO_SYMBOLS = "tsyms"

        private const val DEFAULT_CURRENCY = "USD"
    }
}