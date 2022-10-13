package ua.blackwindstudio.cryptoexchangeapp.coin.data

import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.mapLatest
import ua.blackwindstudio.cryptoexchangeapp.App
import ua.blackwindstudio.cryptoexchangeapp.coin.data.db.CoinDatabase
import ua.blackwindstudio.cryptoexchangeapp.coin.data.network.workers.RefreshCoinPricesDataWorker
import ua.blackwindstudio.cryptoexchangeapp.coin.data.network.workers.RefreshTopCoinsListWorker

@ExperimentalCoroutinesApi
object CoinRepository {
    val context = App.INSTANCE ?: throw RuntimeException("Application not yet initialized")
    private val db = CoinDatabase.instance(context).dao

    fun updatePriceList(toSymbol: String) {
        initializeRepository(toSymbol)
    }

    private fun initializeRepository(
        toSymbol: String
    ) {
        val workManager = WorkManager.getInstance(context)
        queueRefreshTopCoinsList(workManager)
        queueRefreshCoinPricesWork(toSymbol, workManager)
    }

    private fun queueRefreshTopCoinsList(workManager: WorkManager) {
        workManager.enqueueUniquePeriodicWork(
            RefreshTopCoinsListWorker.NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            RefreshTopCoinsListWorker.makeRequest(DATA_LOAD_LIMIT)
        )
    }

    private fun queueRefreshCoinPricesWork(
        toSymbol: String,
        workManager: WorkManager
    ) {
        workManager.enqueueUniqueWork(
            RefreshCoinPricesDataWorker.NAME,
            ExistingWorkPolicy.REPLACE,
            RefreshCoinPricesDataWorker.makeRequest(toSymbol, DATA_LOAD_DELAY)
        )
    }

    fun getPriceList() = db.getPriceList().mapLatest { list -> list.sortedBy { it.fromSymbol } }

    fun getCoinBySymbol(fromSymbol: String) =
        db.getPriceBySymbols(fromSymbol)

    fun changeToSymbol(toSymbol: String) {
        val workManager = WorkManager.getInstance(context)
        queueRefreshCoinPricesWork(toSymbol, workManager)
    }

    private const val DATA_LOAD_DELAY = 10000L
    private const val DATA_LOAD_LIMIT = 50
}
