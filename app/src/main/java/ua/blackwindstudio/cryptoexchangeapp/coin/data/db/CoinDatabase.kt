package ua.blackwindstudio.cryptoexchangeapp.coin.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ua.blackwindstudio.cryptoexchangeapp.coin.data.db.model.CoinDbModel

@Database(
    entities = [CoinDbModel::class],
    version = 1,
    exportSchema = false
)
abstract class CoinDatabase: RoomDatabase() {
    abstract val dao: CoinDao

    companion object {
        private const val DB_NAME = "coin.db"

        @Volatile
        private var INSTANCE: CoinDatabase? = null

        fun instance(context: Context): CoinDatabase {
            INSTANCE?.let { return INSTANCE as CoinDatabase }

            INSTANCE =
                Room.databaseBuilder(
                    context,
                    CoinDatabase::class.java,
                    DB_NAME
                ).build()
            return INSTANCE as CoinDatabase
        }
    }
}