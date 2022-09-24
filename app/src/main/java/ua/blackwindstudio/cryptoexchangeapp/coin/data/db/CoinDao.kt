package ua.blackwindstudio.cryptoexchangeapp.coin.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

import ua.blackwindstudio.cryptoexchangeapp.coin.data.db.model.CoinDbModel

@Dao
interface CoinDao {
    @Query("SELECT * FROM full_price_info ORDER BY lastUpdate DESC")
    fun getPriceList(): Flow<CoinDbModel>

    @Query("SELECT * FROM full_price_info WHERE fromSymbol == :symbol")
    suspend fun getPriceBySymbol(symbol: String): CoinDbModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPriceList(list: List<CoinDbModel>)
}