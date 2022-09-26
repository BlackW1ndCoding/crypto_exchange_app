package ua.blackwindstudio.cryptoexchangeapp.coin.data

import android.util.Log
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import ua.blackwindstudio.cryptoexchangeapp.App
import ua.blackwindstudio.cryptoexchangeapp.coin.data.db.CoinDatabase
import ua.blackwindstudio.cryptoexchangeapp.coin.data.mapper.CoinMapper
import ua.blackwindstudio.cryptoexchangeapp.coin.data.network.CoinApiFactory
import ua.blackwindstudio.cryptoexchangeapp.coin.data.network.model.CoinNamesListDto
import ua.blackwindstudio.cryptoexchangeapp.coin.data.network.model.CoinsInfoContainerDto

object CoinRepository {
    private val mapper = CoinMapper()

    private val coinApi = CoinApiFactory.apiService
    private val db = CoinDatabase.instance(
        App.INSTANCE?.applicationContext
            ?: throw RuntimeException("Application not yet initialized")
    )

    fun getPriceList() = db.dao.getPriceList()

    suspend fun getCoinBySymbol(symbol: String) = db.dao.getPriceBySymbol(symbol)

    private suspend fun getFullCoinsPriceInfo(
        fromSymbols: String,
        toSymbol: String
    ): CoinsInfoContainerDto {
        val response = coinApi.getFullPriceListByCurrency(fSyms = fromSymbols, tSyms = toSymbol)
        return if (response.isSuccessful) {
            response.body() ?: throw Exception("Response body doesn't exist!!")
        } else {
            Log.d("API_DEBUG", "Response Fail")
            CoinsInfoContainerDto(JsonObject())
        }
    }

    private suspend fun getTopCoins(limit: Int = 10): CoinNamesListDto {
        val response = coinApi.getTopCoinsInfoByCurrency(limit = limit)
        return if (response.isSuccessful) {
            response.body() ?: CoinNamesListDto(emptyList())
        } else {
            CoinNamesListDto(emptyList())
        }
    }

    private fun convertTopCoinsInfoToList(coinNamesListDto: CoinNamesListDto): String {
        return coinNamesListDto.names.map { fullData ->
            fullData.coinName?.name ?: "ERROR"
        }.joinToString(",")
    }

    suspend fun loadData(limit: Int, toSymbol: String) {
        withContext(Dispatchers.IO) {
            while (true) {
                try {
                    val coinSymbols = getTopCoins(limit)
                    val coinsInfoContainer = getFullCoinsPriceInfo(
                        convertTopCoinsInfoToList(coinSymbols), toSymbol
                    )
                    val coinInfoList = mapper.mapExchangeInfoToListCoinInfo(coinsInfoContainer)

                    db.dao.insertPriceList(coinInfoList.map { mapper.mapDtoToDb(it) })
                } catch (e: Exception) {
                    Log.d("Exception", e.message.toString())
                }
                delay(DATA_LOAD_DELAY)
            }
        }
    }

    private const val DATA_LOAD_DELAY = 10000L
}