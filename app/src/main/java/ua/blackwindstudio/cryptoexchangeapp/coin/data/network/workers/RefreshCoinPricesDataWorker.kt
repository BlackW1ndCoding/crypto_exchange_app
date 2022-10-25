package ua.blackwindstudio.cryptoexchangeapp.coin.data.network.workers

import android.content.Context
import android.util.Log
import androidx.work.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.delay
import ua.blackwindstudio.cryptoexchangeapp.coin.data.db.CoinDatabase
import ua.blackwindstudio.cryptoexchangeapp.coin.data.mapper.CoinMapper
import ua.blackwindstudio.cryptoexchangeapp.coin.data.network.CoinRemoteDataSource
import java.time.Duration

class RefreshCoinPricesDataWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val db: CoinDatabase,
    private val remote: CoinRemoteDataSource,
    private val mapper: CoinMapper
): CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val toSymbol = inputData.getString(TO_SYMBOL_PARAM) ?: return Result.failure()
        val delay = inputData.getLong(DATA_LOAD_DELAY_PARAM, DATA_LOAD_DELAY_PARAM_DEFAULT)

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
            delay(delay)
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(context: Context, params: WorkerParameters): RefreshCoinPricesDataWorker
    }

    companion object {
        const val NAME = "RefreshDataWorker"
        const val DATA_LOAD_DELAY_PARAM = "DATA_LOAD_DELAY_PARAM"
        const val DATA_LOAD_DELAY_PARAM_DEFAULT = 10000L
        const val TO_SYMBOL_PARAM = "TO_SYMBOL_PARAM"

        fun makeRequest(
            toSymbol: String,
            delay: Long
        ): PeriodicWorkRequest {
            return PeriodicWorkRequestBuilder<RefreshCoinPricesDataWorker>(Duration.ofMinutes(5))
                .setInputData(
                    workDataOf(
                        TO_SYMBOL_PARAM to toSymbol,
                        DATA_LOAD_DELAY_PARAM to delay
                    )
                ).build()
        }
    }
}