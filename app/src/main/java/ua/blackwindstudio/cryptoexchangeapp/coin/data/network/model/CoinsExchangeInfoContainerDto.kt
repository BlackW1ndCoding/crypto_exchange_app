package ua.blackwindstudio.cryptoexchangeapp.coin.data.network.model

import com.google.gson.annotations.SerializedName
import org.json.JSONObject

data class CoinsExchangeInfoContainerDto(
    @SerializedName("RAW")
    val rawData: JSONObject
)
