package ua.blackwindstudio.cryptoexchangeapp.coin.data.network.workers

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import javax.inject.Inject

class BaseWorkerFactory @Inject constructor(
    private val refreshCoinPricesDataWorkerFactory: RefreshCoinPricesDataWorker.Factory,
    private val refreshTopCoinsListWorkerFactory: RefreshTopCoinsListWorker.Factory
): WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        return when (workerClassName) {
            RefreshCoinPricesDataWorker::class.java.name ->
                refreshCoinPricesDataWorkerFactory.create(appContext, workerParameters)
            RefreshTopCoinsListWorker::class.java.name ->
                refreshTopCoinsListWorkerFactory.create(appContext, workerParameters)
            else -> null
        }
    }
}