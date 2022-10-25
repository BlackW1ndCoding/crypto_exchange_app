package ua.blackwindstudio.cryptoexchangeapp.coin.data

import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.WorkManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.mapLatest
import ua.blackwindstudio.cryptoexchangeapp.coin.data.db.CoinDatabase
import ua.blackwindstudio.cryptoexchangeapp.coin.data.network.CoinPriceUpdateService
import ua.blackwindstudio.cryptoexchangeapp.coin.data.network.workers.RefreshCoinPricesDataWorker
import ua.blackwindstudio.cryptoexchangeapp.coin.data.network.workers.RefreshTopCoinsListWorker
import javax.inject.Inject

@ExperimentalCoroutinesApi
class CoinRepositoryImpl @Inject constructor(
    private val db: CoinDatabase,
    private val priceLoadService: CoinPriceUpdateService,
    private val workManager: WorkManager
): CoinRepository {

    override suspend fun updatePriceList(toSymbol: String, loadDelay: Long) {
        initializeRepository(toSymbol, loadDelay)
    }


    private suspend fun initializeRepository(
        toSymbol: String,
        loadDelay: Long
    ) {
        queueRefreshTopCoinsList()
        stopBackGroundPriceUpdate()
        startPriceLoadingService(toSymbol, loadDelay)
    }

    private suspend fun startPriceLoadingService(toSymbol: String, loadDelay: Long) {
        priceLoadService.startCoinPriceLoading(toSymbol, loadDelay)
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
        workManager.enqueueUniquePeriodicWork(
            RefreshCoinPricesDataWorker.NAME,
            ExistingPeriodicWorkPolicy.REPLACE,
            RefreshCoinPricesDataWorker.makeRequest(toSymbol, DATA_LOAD_DELAY)
        )
    }

    private fun stopBackGroundPriceUpdate() {
        workManager.cancelUniqueWork(RefreshCoinPricesDataWorker.NAME)
    }

    override fun getPriceList() =
        db.dao.getPriceList().mapLatest { list -> list.sortedBy { it.fromSymbol } }

    override fun getCoinBySymbol(fromSymbol: String) =
        db.dao.getPriceBySymbols(fromSymbol)

    override suspend fun changeToSymbol(toSymbol: String) {
        startPriceLoadingService(toSymbol, DATA_LOAD_DELAY)
    }

    override fun onAppDestroy() {
        queueRefreshCoinPricesWork(toSymbol = "USD")
        priceLoadService.cancelCoinPriceLoading()

    }

    companion object {
        private const val DATA_LOAD_DELAY = 10000L
        private const val DATA_LOAD_LIMIT = 50
    }

}
