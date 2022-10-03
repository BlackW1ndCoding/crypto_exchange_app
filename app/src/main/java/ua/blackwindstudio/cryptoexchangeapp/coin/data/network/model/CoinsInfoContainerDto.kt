package ua.blackwindstudio.cryptoexchangeapp.coin.data.network.model

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName

data class CoinsInfoContainerDto(
    @SerializedName("RAW")
    val rawData: JsonObject?
)
