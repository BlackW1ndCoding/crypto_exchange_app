package ua.blackwindstudio.cryptoexchangeapp.coin.data

import androidx.work.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.mapLatest
import ua.blackwindstudio.cryptoexchangeapp.App
import ua.blackwindstudio.cryptoexchangeapp.coin.data.db.CoinDatabase
import ua.blackwindstudio.cryptoexchangeapp.coin.data.db.model.CoinFromSymbolsDbModel
import ua.blackwindstudio.cryptoexchangeapp.coin.data.mapper.CoinMapper
import ua.blackwindstudio.cryptoexchangeapp.coin.data.network.CoinRemoteDataSource
import ua.blackwindstudio.cryptoexchangeapp.coin.data.network.workers.RefreshCoinPricesDataWorker
import ua.blackwindstudio.cryptoexchangeapp.coin.data.network.workers.RefreshTopCoinsListWorker

@ExperimentalCoroutinesApi
object CoinRepository {
    private val mapper = CoinMapper()
    private val remote = CoinRemoteDataSource()
    private val db = CoinDatabase.instance(
        App.INSTANCE?.applicationContext
            ?: throw RuntimeException("Application not yet initialized")
    ).dao

    fun updatePriceList(limit: Int, toSymbol: String) {
        initializeRepository(limit, toSymbol)
    }

    private fun initializeRepository(
        limit: Int,
        toSymbol: String
    ) {
        val workManager = WorkManager.getInstance(
            App.INSTANCE?.applicationContext
                ?: throw RuntimeException("Application not yet initialized")
        )
        queueRefreshCoinPricesWork(toSymbol, workManager)
        queueRefreshTopCoinsList(workManager)
    }

    private fun queueRefreshTopCoinsList(workManager: WorkManager) {
        workManager.enqueueUniquePeriodicWork(
            RefreshTopCoinsListWorker.NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            RefreshTopCoinsListWorker.makeRequest()
        )
    }

    private fun queueRefreshCoinPricesWork(toSymbol: String, workManager: WorkManager) {
        workManager.enqueueUniqueWork(
            RefreshCoinPricesDataWorker.NAME,
            ExistingWorkPolicy.REPLACE,
            RefreshCoinPricesDataWorker.makeRequest(toSymbol)
        )
    }

    fun getPriceList() = db.getPriceList().mapLatest { list -> list.sortedBy { it.fromSymbol } }

    fun getCoinBySymbol(fromSymbol: String) =
        db.getPriceBySymbols(fromSymbol)

    fun changeToSymbol(toSymbol: String) {
        val workManager = WorkManager.getInstance(
            App.INSTANCE?.applicationContext
                ?: throw RuntimeException("Application not yet initialized")
        )
        queueRefreshCoinPricesWork(toSymbol, workManager)
    }

    private const val DATA_LOAD_DELAY = 10000L
}
