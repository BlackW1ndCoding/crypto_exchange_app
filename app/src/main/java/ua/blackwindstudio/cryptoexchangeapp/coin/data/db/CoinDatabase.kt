package ua.blackwindstudio.cryptoexchangeapp.coin.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ua.blackwindstudio.cryptoexchangeapp.coin.data.db.model.CoinDbModel
import ua.blackwindstudio.cryptoexchangeapp.coin.data.db.model.CoinFromSymbolsDbModel
import ua.blackwindstudio.cryptoexchangeapp.coin.data.db.model.CoinToSymbolDbModel

@Database(
    entities = [CoinDbModel::class, CoinFromSymbolsDbModel::class, CoinToSymbolDbModel::class],
    version = 1,
    exportSchema = false
)
abstract class CoinDatabase: RoomDatabase() {
    abstract val dao: CoinDao

    companion object {
        const val DB_NAME = "coin.db"
    }
}