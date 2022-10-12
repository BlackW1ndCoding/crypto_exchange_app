package ua.blackwindstudio.cryptoexchangeapp.coin.data.network.workers

import android.app.Application
import android.util.Log
import androidx.work.*
import kotlinx.coroutines.delay
import ua.blackwindstudio.cryptoexchangeapp.App
import ua.blackwindstudio.cryptoexchangeapp.coin.data.db.CoinDatabase
import ua.blackwindstudio.cryptoexchangeapp.coin.data.mapper.CoinMapper
import ua.blackwindstudio.cryptoexchangeapp.coin.data.network.CoinRemoteDataSource

class RefreshCoinPricesDataWorker(
    context: Application,
    private val params: WorkerParameters
): CoroutineWorker(context, params) {

    private val remote = CoinRemoteDataSource()
    private val db = CoinDatabase.instance(
        App.INSTANCE?.applicationContext
            ?: throw RuntimeException("Application not yet initialized")
    ).dao
    private val mapper = CoinMapper()

    override suspend fun doWork(): Result {
        //TODO finish workers transition
        val toSymbol = params.inputData.getString(TO_SYMBOL_PARAM) ?: return Result.failure()
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
            delay(10000L)
        }
    }

    companion object {
        const val NAME = "RefreshDataWorker"
        const val TO_SYMBOL_PARAM = "TO_SYMBOL_PARAM"

        fun makeRequest(
            toSymbol: String
        ): OneTimeWorkRequest {
            return OneTimeWorkRequestBuilder<RefreshCoinPricesDataWorker>()
                .setInputData(
                    Data.Builder()
                        .putString(
                            TO_SYMBOL_PARAM,
                            toSymbol
                        ).build()
                ).build()
        }
    }
}