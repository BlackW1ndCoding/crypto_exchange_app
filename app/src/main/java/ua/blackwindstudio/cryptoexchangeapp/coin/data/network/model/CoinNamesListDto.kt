package ua.blackwindstudio.cryptoexchangeapp.coin.data.network.model

import com.google.gson.annotations.SerializedName

data class CoinNamesListDto(
    @SerializedName("Data")
    val names: List<CoinNameContainerDto>
)
