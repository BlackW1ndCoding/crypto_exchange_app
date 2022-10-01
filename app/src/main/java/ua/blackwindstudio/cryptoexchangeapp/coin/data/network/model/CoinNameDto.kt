package ua.blackwindstudio.cryptoexchangeapp.coin.data.network.model

import com.google.gson.annotations.SerializedName

data class CoinNameDto(
    @SerializedName("Name")
    val name: String,
    @SerializedName("ImageUrl")
    val imageUrl: String
)
