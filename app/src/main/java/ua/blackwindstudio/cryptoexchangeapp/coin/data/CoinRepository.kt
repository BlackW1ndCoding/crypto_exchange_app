package ua.blackwindstudio.cryptoexchangeapp.coin.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import ua.blackwindstudio.cryptoexchangeapp.coin.data.db.model.CoinDbModel
import ua.blackwindstudio.cryptoexchangeapp.coin.data.db.model.CoinToSymbolDbModel
import ua.blackwindstudio.cryptoexchangeapp.coin.data.network.CoinError

interface CoinRepository {

    val errorsChannel: SharedFlow<CoinError>

    fun getPriceList(): Flow<List<CoinDbModel>>

    fun getCoinBySymbol(fromSymbol: String): Flow<CoinDbModel>

    fun getToSymbol(): Flow<CoinToSymbolDbModel>

    fun changeToSymbol(toSymbol: String)

    fun onAppDestroy()
}