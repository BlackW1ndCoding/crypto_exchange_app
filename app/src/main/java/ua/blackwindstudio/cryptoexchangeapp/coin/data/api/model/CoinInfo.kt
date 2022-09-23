package ua.blackwindstudio.cryptoexchangeapp.coin.data.api.model

import com.google.gson.annotations.SerializedName

data class CoinInfo(
    @SerializedName("Name")
    val name: String,
    @SerializedName("ImageUrl")
    val imageUrl: String
)
