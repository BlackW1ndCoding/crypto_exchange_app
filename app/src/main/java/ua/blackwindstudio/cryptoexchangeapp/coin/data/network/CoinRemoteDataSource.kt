package ua.blackwindstudio.cryptoexchangeapp.coin.data.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ua.blackwindstudio.cryptoexchangeapp.coin.data.network.model.CoinNamesListDto
import ua.blackwindstudio.cryptoexchangeapp.coin.data.network.model.CoinsInfoContainerDto

class CoinRemoteDataSource {
    private val coinApi = CoinApiFactory.apiService

    suspend fun fetchFullCoinsPriceInfo(
        fromSymbols: String,
        toSymbol: String
    ): CoinsInfoContainerDto {
        return withContext(Dispatchers.IO) {
            val response =
                coinApi.getFullPriceListByCurrency(fSyms = fromSymbols, tSyms = toSymbol)
            if (response.isSuccessful) {
                response.body() ?: throw Exception("Response has empty body")
            } else {
                throw Exception("Could not fetch coin price info")
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