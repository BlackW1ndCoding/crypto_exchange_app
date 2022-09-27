package ua.blackwindstudio.cryptoexchangeapp.coin.data.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "from_symbols")
data class CoinFromSymbolsDbModel(
    @PrimaryKey
    val fromSymbols: String
)
