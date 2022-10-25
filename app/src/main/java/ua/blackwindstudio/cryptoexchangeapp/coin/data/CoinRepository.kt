package ua.blackwindstudio.cryptoexchangeapp.coin.data

import kotlinx.coroutines.flow.Flow
import ua.blackwindstudio.cryptoexchangeapp.coin.data.db.model.CoinDbModel

interface CoinRepository {
    fun updatePriceList(toSymbol: String)

    fun getPriceList(): Flow<List<CoinDbModel>>

    fun getCoinBySymbol(fromSymbol: String): Flow<CoinDbModel>

    fun changeToSymbol(toSymbol: String)
}