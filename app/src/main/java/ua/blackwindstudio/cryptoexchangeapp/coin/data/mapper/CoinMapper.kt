package ua.blackwindstudio.cryptoexchangeapp.coin.data.mapper

import com.google.gson.Gson
import com.google.gson.JsonElement
import ua.blackwindstudio.cryptoexchangeapp.coin.data.db.model.CoinDbModel
import ua.blackwindstudio.cryptoexchangeapp.coin.data.network.model.CoinInfoDto
import ua.blackwindstudio.cryptoexchangeapp.coin.data.network.model.CoinNamesListDto
import ua.blackwindstudio.cryptoexchangeapp.coin.data.network.model.CoinsInfoContainerDto

class CoinMapper {

    fun mapDtoToDb(dto: CoinInfoDto): CoinDbModel =
        CoinDbModel(
            fromSymbol = dto.fromSymbol,
            toSymbol = dto.toSymbol ?: "",
            price = dto.price ?: "",
            lastUpdate = dto.lastUpdate ?: "",
            highDay = dto.highDay ?: "",
            lowDay = dto.lowDay ?: "",
            lastMarket = dto.lastMarket ?: "",
            imageUrl = BASE_IMAGE_URL + dto.imageUrl
        )

    fun mapExchangeInfoToListCoinInfo(json: CoinsInfoContainerDto): List<CoinInfoDto> {
        val result = mutableListOf<CoinInfoDto>()
        val jsonObject = json.rawData
        val keys = jsonObject.keySet()
        for (coinKey in keys) {
            val coinJson = jsonObject.getAsJsonObject(coinKey)
            val currencyKeySet = coinJson.keySet()
            for (currencyKey in currencyKeySet) {
                val currencyJson: JsonElement = coinJson.getAsJsonObject(currencyKey)
                val priceInfo: CoinInfoDto = Gson().fromJson(
                    currencyJson,
                    CoinInfoDto::class.java
                )
                result.add(priceInfo)
            }
        }

        return result
    }

    fun convertTopCoinsInfoDtoToString(coinNamesListDto: CoinNamesListDto): String {
        return coinNamesListDto.names.joinToString(",") { fullData ->
            fullData.coinName?.name ?: "ERROR"
        }
    }

    companion object {
        private const val BASE_IMAGE_URL = "https://www.cryptocompare.com/"
    }
}