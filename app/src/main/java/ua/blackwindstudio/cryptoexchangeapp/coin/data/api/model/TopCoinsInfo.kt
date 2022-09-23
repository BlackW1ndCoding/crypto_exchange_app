package ua.blackwindstudio.cryptoexchangeapp.coin.data.api.model

import com.google.gson.annotations.SerializedName

data class TopCoinsInfo(
    @SerializedName("Data")
    val coinFullData: List<CoinFullData>
)
