package ua.blackwindstudio.cryptoexchangeapp.coin.data.network

import android.util.Log
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import ua.blackwindstudio.cryptoexchangeapp.coin.data.network.model.CoinNamesListDto
import ua.blackwindstudio.cryptoexchangeapp.coin.data.network.model.CoinsInfoContainerDto
import java.io.IOException

class CoinRemoteDataSource {
    private val coinApi = CoinApiFactory.apiService

    fun getCoinPriceUpdates(
        fromSymbols: String,
        toSymbol: String,
        delay: Long
    ): Flow<CoinsInfoContainerDto> = flow {
        withContext(Dispatchers.IO) {
            while (true) {
                fetchFullCoinsPriceInfo(
                    fromSymbols,
                    toSymbol
                )
                delay(delay)
            }

        }
    }


    suspend fun fetchFullCoinsPriceInfo(
        fromSymbols: String,
        toSymbol: String
    ): CoinsInfoContainerDto {
        val response =
            coinApi.getFullPriceListByCurrency(fSyms = fromSymbols, tSyms = toSymbol)
        return if (response.isSuccessful) {
            response.body() ?: throw IOException("Response body doesn't exist!!")
        } else {
            Log.d("API_DEBUG", "Response Fail")
            CoinsInfoContainerDto(JsonObject())
        }
    }

    suspend fun fetchTopCoins(limit: Int = 10): CoinNamesListDto {
        val response = coinApi.getTopCoinsInfoByCurrency(limit = limit)
        return if (response.isSuccessful) {
            response.body() ?: CoinNamesListDto(emptyList())
        } else {
            CoinNamesListDto(emptyList())
        }
    }
}