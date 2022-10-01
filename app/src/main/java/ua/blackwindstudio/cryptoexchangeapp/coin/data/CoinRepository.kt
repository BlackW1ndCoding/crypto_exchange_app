package ua.blackwindstudio.cryptoexchangeapp.coin.data

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.withContext
import ua.blackwindstudio.cryptoexchangeapp.App
import ua.blackwindstudio.cryptoexchangeapp.coin.data.db.CoinDatabase
import ua.blackwindstudio.cryptoexchangeapp.coin.data.db.model.CoinFromSymbolsDbModel
import ua.blackwindstudio.cryptoexchangeapp.coin.data.mapper.CoinMapper
import ua.blackwindstudio.cryptoexchangeapp.coin.data.network.CoinRemoteDataSource

@ExperimentalCoroutinesApi
object CoinRepository {
    private val mapper = CoinMapper()
    private val remote = CoinRemoteDataSource()
    private val db = CoinDatabase.instance(
        App.INSTANCE?.applicationContext
            ?: throw RuntimeException("Application not yet initialized")
    ).dao
    private var fromSymbols: CoinFromSymbolsDbModel? = null
    private var toSymbol: String? = null

    private suspend fun initializeRepository(
        limit: Int,
        toSymbol: String
    ) {
        if (this.toSymbol == null) this.toSymbol = toSymbol
        if (fromSymbols == null) {
            val fromSymbolsDto = remote.fetchTopCoins(limit)

            fromSymbols = CoinFromSymbolsDbModel(
                mapper.convertTopCoinsInfoDtoToString(fromSymbolsDto)
            )
        }
    }

    suspend fun updatePriceList(limit: Int, toSymbol: String) {
        initializeRepository(limit, toSymbol)
        Log.d("FLOW_DEBUG", "Starting update")
        val flow = remote.getCoinPriceUpdates(fromSymbols!!.fromSymbols, toSymbol ?: "USD", 10000L)
        flow.cancellable().collectLatest { dto ->
            Log.d("FLOW_DEBUG", "Collecting")
            val coinInfoList =
                mapper.mapExchangeInfoToListCoinInfo(dto)

            db.insertPriceList(coinInfoList.map {
                mapper.mapDtoToDb(
                    it
                )
            })
        }
    }

    fun getPriceList() = db.getPriceList().mapLatest { list -> list.sortedBy { it.fromSymbol } }

    suspend fun getCoinBySymbol(fromSymbol: String, toSymbol: String) =
        db.getPriceBySymbols(fromSymbol, toSymbol)

    suspend fun loadFromSymbols(limit: Int) {
        val topCoinsList = remote.fetchTopCoins(limit)
        db.insertFromSymbols(
            CoinFromSymbolsDbModel(
                mapper.convertTopCoinsInfoDtoToString(topCoinsList)
            )
        )
    }

    fun changeToSymbol(newToSymbol: String) {
        toSymbol = newToSymbol
    }

    suspend fun loadData() {
        withContext(Dispatchers.IO) {
            while (true) {
                try {
                    val coinFromSymbols = fromSymbols
                    val coinsInfoContainer = remote.fetchFullCoinsPriceInfo(
                        coinFromSymbols?.fromSymbols
                            ?: throw Exception("fromSymbols not yet initialized"),
                        toSymbol ?: throw Exception("toSymbol not yet initialized")
                    )
                    val coinInfoList =
                        mapper.mapExchangeInfoToListCoinInfo(coinsInfoContainer)

                    db.insertPriceList(coinInfoList.map {
                        mapper.mapDtoToDb(
                            it
                        )
                    })
                } catch (e: Exception) {
                    Log.d(
                        "Exception",
                        "Exception while loading price list: ${e.message.toString()}"
                    )
                }
                delay(DATA_LOAD_DELAY)
            }
        }
    }

    private const val DATA_LOAD_DELAY = 10000L
}
