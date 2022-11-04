package ua.blackwindstudio.cryptoexchangeapp.coin.data

import kotlinx.coroutines.flow.Flow
import ua.blackwindstudio.cryptoexchangeapp.coin.data.db.model.CoinDbModel
import ua.blackwindstudio.cryptoexchangeapp.coin.data.db.model.CoinToSymbolDbModel

interface CoinRepository {
    fun getPriceList(): Flow<List<CoinDbModel>>

    fun getCoinBySymbol(fromSymbol: String): Flow<CoinDbModel>

    fun getToSymbol(): Flow<CoinToSymbolDbModel>

    fun changeToSymbol(toSymbol: String)

    fun onAppDestroy()
}