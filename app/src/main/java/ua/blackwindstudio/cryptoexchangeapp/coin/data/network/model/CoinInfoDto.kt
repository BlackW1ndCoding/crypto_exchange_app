package ua.blackwindstudio.cryptoexchangeapp.coin.data.network.model

import com.google.gson.annotations.SerializedName

data class CoinInfoDto(
    @SerializedName("FROMSYMBOL")
    val fromSymbol: String,
    @SerializedName("TOSYMBOL")
    val toSymbol: String?,
    @SerializedName("PRICE")
    val price: String?,
    @SerializedName("LASTUPDATE")
    val lastUpdate: String?,
    @SerializedName("HIGHDAY")
    val highDay: String?,
    @SerializedName("LOWDAY")
    val lowDay: String?,
    @SerializedName("LASTMARKET")
    val lastMarket: String?,
    @SerializedName("IMAGEURL")
    val imageUrl: String?
)
