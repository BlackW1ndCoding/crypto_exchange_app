package ua.blackwindstudio.cryptoexchangeapp.coin.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ua.blackwindstudio.cryptoexchangeapp.coin.data.db.model.CoinDbModel
import ua.blackwindstudio.cryptoexchangeapp.coin.data.db.model.CoinFromSymbolsDbModel

@Database(
    entities = [CoinDbModel::class, CoinFromSymbolsDbModel::class],
    version = 1,
    exportSchema = false
)
abstract class CoinDatabase: RoomDatabase() {

    lateinit var INSTANCE: CoinDatabase

    abstract val dao: CoinDao

    companion object {
        const val DB_NAME = "coin.db"
    }
}