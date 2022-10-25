package ua.blackwindstudio.cryptoexchangeapp.coin.data

import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.mapLatest
import ua.blackwindstudio.cryptoexchangeapp.coin.data.db.CoinDatabase
import ua.blackwindstudio.cryptoexchangeapp.coin.data.network.workers.RefreshCoinPricesDataWorker
import ua.blackwindstudio.cryptoexchangeapp.coin.data.network.workers.RefreshTopCoinsListWorker

@ExperimentalCoroutinesApi
class CoinRepositoryImpl (
    private val db: CoinDatabase,
    private val workManager: WorkManager
): CoinRepository {

    override fun updatePriceList(toSymbol: String) {
        initializeRepository(toSymbol)
    }

    private fun initializeRepository(
        toSymbol: String
    ) {
        queueRefreshTopCoinsList()
        queueRefreshCoinPricesWork(toSymbol)
    }

    private fun queueRefreshTopCoinsList() {
        workManager.enqueueUniquePeriodicWork(
            RefreshTopCoinsListWorker.NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            RefreshTopCoinsListWorker.makeRequest(DATA_LOAD_LIMIT)
        )
    }

    private fun queueRefreshCoinPricesWork(
        toSymbol: String
    ) {
        workManager.enqueueUniqueWork(
            RefreshCoinPricesDataWorker.NAME,
            ExistingWorkPolicy.REPLACE,
            RefreshCoinPricesDataWorker.makeRequest(toSymbol, DATA_LOAD_DELAY)
        )
    }

    override fun getPriceList() =
        db.dao.getPriceList().mapLatest { list -> list.sortedBy { it.fromSymbol } }

    override fun getCoinBySymbol(fromSymbol: String) =
        db.dao.getPriceBySymbols(fromSymbol)

    override fun changeToSymbol(toSymbol: String) {
        queueRefreshCoinPricesWork(toSymbol)
    }

    companion object {
        private const val DATA_LOAD_DELAY = 10000L
        private const val DATA_LOAD_LIMIT = 50
    }

}
