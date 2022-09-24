package ua.blackwindstudio.cryptoexchangeapp.coin.data

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import org.json.JSONObject
import ua.blackwindstudio.cryptoexchangeapp.App
import ua.blackwindstudio.cryptoexchangeapp.coin.data.db.CoinDatabase
import ua.blackwindstudio.cryptoexchangeapp.coin.data.mapper.CoinMapper
import ua.blackwindstudio.cryptoexchangeapp.coin.data.network.CoinApiFactory
import ua.blackwindstudio.cryptoexchangeapp.coin.data.network.model.CoinsExchangeInfoContainerDto
import ua.blackwindstudio.cryptoexchangeapp.coin.data.network.model.CoinNamesListDto

object CoinRepository {

    private val coinApi = CoinApiFactory.apiService
    private val db = CoinDatabase.instance(
        App.INSTANCE?.applicationContext
            ?: throw RuntimeException("Application not yet initialized")
    )

    fun getPriceList() = db.dao.getPriceList()

    suspend fun getCoinBySymbol(symbol: String) = db.dao.getPriceBySymbol(symbol)

    suspend fun loadNewPricesIntoDB(limit: Int, currency: String) {
        val mapper = CoinMapper()
        val topCoinsList = convertTopCoinsInfoToList(getTopCoins(limit))
        val fullCoinsPriceInfo = getFullCoinsPriceInfo(
            topCoinsList.joinToString(","),
            currency
        )
        val coinInfoDtoList = mapper.mapExchangeInfoToListCoinInfo(fullCoinsPriceInfo)
        db.dao.insertPriceList(
            coinInfoDtoList.map { dto -> mapper.mapDtoToDb(dto) }
        )
    }

    private suspend fun getFullCoinsPriceInfo(
        fromCoins: String,
        toCurrency: String
    ): CoinsExchangeInfoContainerDto {
        return withContext(Dispatchers.IO) {
            val result: CoinsExchangeInfoContainerDto = async {
                val response =
                    coinApi.getFullPriceListByCurrency(fSyms = fromCoins, tSyms = toCurrency)
                return@async if (response.isSuccessful) {
                    Log.d("API_DEBUG", "attemping iteration")
                    Log.d("API_DEBUG","${response.body()?.rawData}")
                    response.body()?.rawData?.keys().let {
                        for(r in it as Iterator<String>){
                            Log.d("API_DEBUG", "${r}")
                        }

                    }

                    response.body() ?: CoinsExchangeInfoContainerDto(JSONObject("error"))
                } else {
                    CoinsExchangeInfoContainerDto(JSONObject())
                }
            }.await()
            result
        }
    }

    private suspend fun getTopCoins(limit: Int = 10): CoinNamesListDto {
        return withContext(Dispatchers.IO) {
            val result: CoinNamesListDto = async {
                val response = coinApi.getTopCoinsInfoByCurrency(limit = limit)
                return@async if (response.isSuccessful) {
                    response.body() ?: CoinNamesListDto(emptyList())
                } else {
                    CoinNamesListDto(emptyList())
                }
            }.await()
            result
        }
    }

    private fun convertTopCoinsInfoToList(coinNamesListDto: CoinNamesListDto): List<String> {
        return coinNamesListDto.names.map { fullData ->
            fullData.coinName?.name ?: "ERROR"
        }
    }
}