package ua.blackwindstudio.cryptoexchangeapp.coin.data.network.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import ua.blackwindstudio.cryptoexchangeapp.coin.data.db.CoinDatabase
import ua.blackwindstudio.cryptoexchangeapp.coin.data.db.model.CoinFromSymbolsDbModel
import ua.blackwindstudio.cryptoexchangeapp.coin.data.mapper.CoinMapper
import ua.blackwindstudio.cryptoexchangeapp.coin.data.network.CoinRemoteDataSource
import java.time.Duration

class RefreshTopCoinsListWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val db: CoinDatabase,
    private val remote: CoinRemoteDataSource,
    private val mapper: CoinMapper
):
    CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val limit = inputData.getInt(LIMIT_PARAM, DEFAULT_LIMIT_PARAM)
        val response = remote.fetchTopCoins(limit)
        return if (response.names != null) {
            db.dao.insertFromSymbols(
                CoinFromSymbolsDbModel(
                    mapper.convertTopCoinsInfoDtoToString(response)
                )
            )
            Result.success()
        } else {
            Result.retry()
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(context: Context, params: WorkerParameters): RefreshTopCoinsListWorker
    }

    companion object {
        const val NAME = "RefreshTopCoinsListWorker"
        const val LIMIT_PARAM = "LIMIT_PARAM"
        private const val DEFAULT_LIMIT_PARAM = 50

        fun makeRequest(limit: Int): PeriodicWorkRequest {
            return PeriodicWorkRequest.Builder(
                RefreshTopCoinsListWorker::class.java,
                Duration.ofHours(3)
            ).setInputData(
                workDataOf(LIMIT_PARAM to limit)
            ).build()
        }
    }
}