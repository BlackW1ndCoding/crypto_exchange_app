package ua.blackwindstudio.cryptoexchangeapp.coin.data

import kotlinx.coroutines.flow.Flow
import ua.blackwindstudio.cryptoexchangeapp.coin.data.db.model.CoinDbModel

interface CoinRepository {
    suspend fun updatePriceList(toSymbol: String, loadDelay: Long)

    fun getPriceList(): Flow<List<CoinDbModel>>

    fun getCoinBySymbol(fromSymbol: String): Flow<CoinDbModel>

    suspend fun changeToSymbol(toSymbol: String)

    fun onAppDestroy()
}