package ua.blackwindstudio.cryptoexchangeapp.coin.ui.model

data class UiCoin(
    val fromSymbol: String,
    val toSymbol: String,
    val price: String,
    val dailyMin: String,
    val dailyMax: String,
    val lastMarket: String,
    val updated: String,
    val imageUrl: String,
)