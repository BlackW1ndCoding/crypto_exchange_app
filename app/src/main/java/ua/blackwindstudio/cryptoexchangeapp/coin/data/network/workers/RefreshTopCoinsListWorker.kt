package ua.blackwindstudio.cryptoexchangeapp.coin.data.network.workers

import android.app.Application
import androidx.work.CoroutineWorker
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkerParameters
import ua.blackwindstudio.cryptoexchangeapp.App
import ua.blackwindstudio.cryptoexchangeapp.coin.data.db.CoinDatabase
import ua.blackwindstudio.cryptoexchangeapp.coin.data.db.model.CoinFromSymbolsDbModel
import ua.blackwindstudio.cryptoexchangeapp.coin.data.mapper.CoinMapper
import ua.blackwindstudio.cryptoexchangeapp.coin.data.network.CoinRemoteDataSource
import java.time.Duration

class RefreshTopCoinsListWorker(
    context: Application,
    private val params: WorkerParameters
):
    CoroutineWorker(context, params) {
    private val db = CoinDatabase.instance(
        App.INSTANCE?.applicationContext
            ?: throw RuntimeException("Application not yet initialized")
    ).dao
    private val remote = CoinRemoteDataSource()
    private val mapper = CoinMapper()

    override suspend fun doWork(): Result {
        val limit = params.inputData.getInt(LIMIT_PARAM, DEFAULT_LIMIT_PARAM)
        val response = remote.fetchTopCoins(limit)
        return if (response.names != null) {
            db.insertFromSymbols(
                CoinFromSymbolsDbModel(
                    mapper.convertTopCoinsInfoDtoToString(response)
                )
            )
            Result.success()
        } else {
            Result.retry()
        }
    }

    companion object {
        const val NAME = "RefreshTopCoinsListWorker"
        const val LIMIT_PARAM = "LIMIT_PARAM"
        private const val DEFAULT_LIMIT_PARAM = 50

        fun makeRequest(): PeriodicWorkRequest {
            return PeriodicWorkRequest.Builder(
                RefreshTopCoinsListWorker::class.java,
                Duration.ofHours(3)
            ).build()
        }
    }
}