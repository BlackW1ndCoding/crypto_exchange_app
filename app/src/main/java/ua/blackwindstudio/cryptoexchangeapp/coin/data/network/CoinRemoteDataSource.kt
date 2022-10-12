package ua.blackwindstudio.cryptoexchangeapp.coin.data.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import ua.blackwindstudio.cryptoexchangeapp.coin.data.network.model.CoinNamesListDto
import ua.blackwindstudio.cryptoexchangeapp.coin.data.network.model.CoinsInfoContainerDto

class CoinRemoteDataSource {
    private val coinApi = CoinApiFactory.apiService

    fun getCoinPriceUpdates(
        fromSymbols: String,
        toSymbol: String,
        delay: Long
    ): Flow<CoinsInfoContainerDto> = flow {
        while (true) {
            val info = fetchFullCoinsPriceInfo(
                fromSymbols,
                toSymbol
            )
            emit(info)
            delay(delay)
        }
    }

    private suspend fun fetchFullCoinsPriceInfo(
        fromSymbols: String,
        toSymbol: String
    ): CoinsInfoContainerDto {
        return withContext(Dispatchers.IO) {
            val response =
                coinApi.getFullPriceListByCurrency(fSyms = fromSymbols, tSyms = toSymbol)
            if (response.isSuccessful) {
                response.body() ?: CoinsInfoContainerDto(null)
            } else {
                CoinsInfoContainerDto(null)
            }
        }
    }

    suspend fun fetchTopCoins(limit: Int = 10): CoinNamesListDto {
        val response = coinApi.getTopCoinsInfoByCurrency(limit = limit)
        return if (response.isSuccessful) {
            response.body() ?: CoinNamesListDto(null)
        } else {
            CoinNamesListDto(null)
        }
    }
}