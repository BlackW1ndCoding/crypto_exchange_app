package ua.blackwindstudio.cryptoexchangeapp.coin.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

import ua.blackwindstudio.cryptoexchangeapp.coin.data.db.model.CoinDbModel
import ua.blackwindstudio.cryptoexchangeapp.coin.data.db.model.CoinFromSymbolsDbModel

@Dao
interface CoinDao {
    @Query("SELECT * FROM full_price_info ORDER BY lastUpdate DESC LIMIT 50")
    fun getPriceList(): Flow<List<CoinDbModel>>

    @Query("SELECT * FROM full_price_info WHERE fromSymbol == :fromSymbol")
    suspend fun getPriceBySymbols(fromSymbol: String): CoinDbModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPriceList(list: List<CoinDbModel>)

    @Query("SELECT * FROM from_symbols LIMIT 1")
    fun getFromSymbols(): Flow<CoinFromSymbolsDbModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFromSymbols(fromSymbols: CoinFromSymbolsDbModel)
}