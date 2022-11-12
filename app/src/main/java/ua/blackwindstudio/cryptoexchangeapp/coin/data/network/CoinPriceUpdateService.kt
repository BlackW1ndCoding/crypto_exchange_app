package ua.blackwindstudio.cryptoexchangeapp.coin.data.network

import android.util.Log
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import ua.blackwindstudio.cryptoexchangeapp.coin.data.db.CoinDatabase
import ua.blackwindstudio.cryptoexchangeapp.coin.data.mapper.CoinMapper
import java.util.concurrent.CancellationException
import javax.inject.Inject

class CoinPriceUpdateService @Inject constructor(
    private val remote: CoinRemoteDataSource,
    private val db: CoinDatabase,
    private val mapper: CoinMapper
) {
    private var job = Job()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _errorsChannel = MutableSharedFlow<CoinError.RemoteError>()
    val errorsChannel: SharedFlow<CoinError.RemoteError> = _errorsChannel

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun startCoinPriceLoading(toSymbol: String, loadDelayMs: Long) {

        job.cancel()
        job = Job()

        withContext(IO + job as Job) {
            val logJob = job
            while (isActive) {
                Log.d("JOB_DEBUG", "working at $logJob")
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
                    when (e) {
                        is Exception -> _errorsChannel.emit(CoinError.RemoteError.IOError)
                        else -> Log.d(
                            "RefreshCoinPricesDataWorker",
                            "Could not update prices data with ${e.message}"
                        )
                    }
                }
                delay(loadDelayMs)
            }
        }
    }

    fun cancelCoinPriceLoading() {
        job.cancel(
            CancellationException("Loading canceled")
        )
    }
}