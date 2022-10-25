package ua.blackwindstudio.cryptoexchangeapp.coin.data.network

import android.util.Log
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import ua.blackwindstudio.cryptoexchangeapp.coin.data.db.CoinDatabase
import ua.blackwindstudio.cryptoexchangeapp.coin.data.mapper.CoinMapper
import java.util.concurrent.CancellationException
import javax.inject.Inject

class CoinPriceUpdateService @Inject constructor(
    private val remote: CoinRemoteDataSource,
    private val db: CoinDatabase,
    private val mapper: CoinMapper
) {
    private val job = Job()

    suspend fun startCoinPriceLoading(toSymbol: String, loadDelay: Long){
        withContext(IO + job) {
            while (true) {
                try {
                    val fromSymbolsDb = db.dao.getFromSymbols()
                    val response =
                        remote.fetchFullCoinsPriceInfo(
                            fromSymbols = fromSymbolsDb.fromSymbols,
                            toSymbol = toSymbol
                        )

                    db.dao.insertPriceList(
                        mapper.mapExchangeInfoToListCoinInfo(
                            response
                        ).map { mapper.mapDtoToDb(it) }
                    )
                } catch (e: Exception) {
                    Log.d(
                        "RefreshCoinPricesDataWorker",
                        "Could not update prices data with ${e.message}"
                    )
                }
                delay(loadDelay)
            }
        }
    }

    fun cancelCoinPriceLoading(){
        job.cancel(
            CancellationException("Loading canceled")
        )
    }
}