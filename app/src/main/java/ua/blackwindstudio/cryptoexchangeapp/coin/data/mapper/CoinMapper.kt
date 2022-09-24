package ua.blackwindstudio.cryptoexchangeapp.coin.data.mapper

import com.google.gson.Gson
import com.google.gson.JsonElement
import ua.blackwindstudio.cryptoexchangeapp.coin.data.db.model.CoinDbModel
import ua.blackwindstudio.cryptoexchangeapp.coin.data.network.model.CoinInfoDto
import ua.blackwindstudio.cryptoexchangeapp.coin.data.network.model.CoinsExchangeInfoContainerDto

class CoinMapper {

    fun mapDtoToDb(dto: CoinInfoDto): CoinDbModel =
        CoinDbModel(
            fromSymbol = dto.fromSymbol,
            toSymbol = dto.toSymbol,
            price = dto.price,
            lastUpdate = dto.lastUpdate,
            highDay = dto.highDay,
            lowDay = dto.lowDay,
            lastMarket = dto.lastMarket,
            imageUrl = BASE_IMAGE_URL + dto.imageUrl
        )

    fun mapExchangeInfoToListCoinInfo(json: CoinsExchangeInfoContainerDto): List<CoinInfoDto> {
        val result = mutableListOf<CoinInfoDto>()
        val jsonObject = json.rawData
        val keys = jsonObject.keys()
        for (coinKey in keys) {
            val coinJson = jsonObject.getJSONObject(coinKey)
            val currencyKeySet = coinJson.keys()
            for (currencyKey in currencyKeySet) {
                val currencyJson: JsonElement = coinJson.getJSONObject(currencyKey) as JsonElement
                val priceInfo: CoinInfoDto = Gson().fromJson(
                    currencyJson,
                    CoinInfoDto::class.java
                )
                result.add(priceInfo)
            }
        }

        return result
    }

    companion object {
        private const val BASE_IMAGE_URL = "https://www.cryptocompare.com/"
    }
}