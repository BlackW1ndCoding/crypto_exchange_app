package ua.blackwindstudio.cryptoexchangeapp.coin.ui.model

data class UiCoin(
    val id: Int,
    val coin: String,
    val currency: String,
    val price: Float,
    val imageUrl: String
)