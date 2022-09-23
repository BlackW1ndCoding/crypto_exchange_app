package ua.blackwindstudio.cryptoexchangeapp.coin.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import ua.blackwindstudio.cryptoexchangeapp.coin.data.api.CoinApiFactory
import ua.blackwindstudio.cryptoexchangeapp.coin.data.api.model.TopCoinsInfo

object CoinRepository {

    private val coinApi = CoinApiFactory.apiService

    suspend fun getTopCoins(limit: Int = 10): TopCoinsInfo {
        return withContext(Dispatchers.IO) {
            val result: TopCoinsInfo = async {
                val response = coinApi.getTopCoinsInfoByCurrency(limit = limit)
                return@async if (response.isSuccessful) {
                    response.body() ?: TopCoinsInfo(emptyList())
                } else {
                    TopCoinsInfo(emptyList())
                }
            }.await()
            result
        }
    }

    private fun convertTopCoinsInfoToList(topCoinsInfo: TopCoinsInfo): List<String> {
        return topCoinsInfo.coinFullData.map { fullData ->
            fullData.coinInfo?.name ?: "ERROR"
        }
    }
}