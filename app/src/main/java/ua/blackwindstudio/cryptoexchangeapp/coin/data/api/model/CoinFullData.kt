package ua.blackwindstudio.cryptoexchangeapp.coin.data.api.model

import com.google.gson.annotations.SerializedName

data class CoinFullData(
    @SerializedName("CoinInfo")
    val coinInfo: CoinInfo?
)