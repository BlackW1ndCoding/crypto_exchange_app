package ua.blackwindstudio.cryptoexchangeapp.coin.ui.mappers

import android.content.Context
import ua.blackwindstudio.cryptoexchangeapp.coin.data.db.model.CoinDbModel
import ua.blackwindstudio.cryptoexchangeapp.coin.ui.model.UiCoin
import ua.blackwindstudio.cryptoexchangeapp.coin.ui.utils.formatDate

class Mapper(private val context: Context) {
    fun mapDbModelToUiCoin(dbModel: CoinDbModel): UiCoin =
        UiCoin(
            fromSymbol = dbModel.fromSymbol,
            toSymbol = dbModel.toSymbol,
            price = dbModel.price,
            dailyMin = dbModel.lowDay,
            dailyMax = dbModel.highDay,
            lastMarket = dbModel.lastMarket,
            updated = formatDate(dbModel.lastUpdate, context),
            imageUrl = dbModel.imageUrl
        )
}