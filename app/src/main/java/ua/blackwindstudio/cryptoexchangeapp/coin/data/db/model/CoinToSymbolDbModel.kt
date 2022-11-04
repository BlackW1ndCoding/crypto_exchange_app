package ua.blackwindstudio.cryptoexchangeapp.coin.data.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "to_symbol")
data class CoinToSymbolDbModel(
    val toSymbol: String,
    @PrimaryKey
    val id: Int = 0
)
