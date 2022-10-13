package ua.blackwindstudio.cryptoexchangeapp.coin.data.network.workers

import android.content.Context
import android.util.Log
import androidx.work.*
import kotlinx.coroutines.delay
import ua.blackwindstudio.cryptoexchangeapp.App
import ua.blackwindstudio.cryptoexchangeapp.coin.data.db.CoinDatabase
import ua.blackwindstudio.cryptoexchangeapp.coin.data.mapper.CoinMapper
import ua.blackwindstudio.cryptoexchangeapp.coin.data.network.CoinRemoteDataSource

class RefreshCoinPricesDataWorker(
    context: Context,
    params: WorkerParameters
): CoroutineWorker(context, params) {

    private val remote = CoinRemoteDataSource()
    private val db = CoinDatabase.instance(
        App.INSTANCE?.applicationContext
            ?: throw RuntimeException("Application not yet initialized")
    ).dao
    private val mapper = CoinMapper()

    override suspend fun doWork(): Result {
        val toSymbol = inputData.getString(TO_SYMBOL_PARAM) ?: return Result.failure()
        val delay = inputData.getLong(DATA_LOAD_DELAY_PARAM, DATA_LOAD_DELAY_PARAM_DEFAULT)

        while (true) {
            try {
                val fromSymbolsDb = db.getFromSymbols()
                val response =
                    remote.fetchFullCoinsPriceInfo(
                        fromSymbols = fromSymbolsDb.fromSymbols,
                        toSymbol = toSymbol
                    )

                db.insertPriceList(
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
            delay(delay)
        }
    }

    companion object {
        const val NAME = "RefreshDataWorker"
        const val DATA_LOAD_DELAY_PARAM = "DATA_LOAD_DELAY_PARAM"
        const val DATA_LOAD_DELAY_PARAM_DEFAULT = 10000L
        const val TO_SYMBOL_PARAM = "TO_SYMBOL_PARAM"

        fun makeRequest(
            toSymbol: String,
            delay: Long
        ): OneTimeWorkRequest {
            return OneTimeWorkRequestBuilder<RefreshCoinPricesDataWorker>()
                .setInputData(
                    workDataOf(
                        TO_SYMBOL_PARAM to toSymbol,
                        DATA_LOAD_DELAY_PARAM to delay
                    )
                ).build()
        }
    }
}