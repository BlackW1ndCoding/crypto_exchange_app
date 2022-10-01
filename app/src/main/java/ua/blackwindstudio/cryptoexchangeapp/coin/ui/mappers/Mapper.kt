package ua.blackwindstudio.cryptoexchangeapp.coin.ui.mappers

import ua.blackwindstudio.cryptoexchangeapp.coin.data.db.model.CoinDbModel
import ua.blackwindstudio.cryptoexchangeapp.coin.ui.model.UiCoin

class Mapper {
    fun mapDbModelToUiCoin(dbModel: CoinDbModel): UiCoin =
        UiCoin(
            fromSymbol = dbModel.fromSymbol,
            toSymbol = dbModel.toSymbol,
            price = dbModel.price,
            updated = dbModel.lastUpdate,
            imageUrl = dbModel.imageUrl
        )
}