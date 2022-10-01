package ua.blackwindstudio.cryptoexchangeapp.coin.data.network.model

import com.google.gson.annotations.SerializedName

data class CoinNameContainerDto(
    @SerializedName("CoinInfo")
    val coinName: CoinNameDto?
)