package ua.blackwindstudio.cryptoexchangeapp.coin.data.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "full_price_info")
data class CoinDbModel(
    @PrimaryKey
    val fromSymbol: String,
    val toSymbol: String,
    val price: String,
    val lastUpdate: String,
    val highDay: String,
    val lowDay: String,
    val lastMarket: String,
    val imageUrl: String
)
