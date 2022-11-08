package ua.blackwindstudio.cryptoexchangeapp.coin.data.mapper

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import io.mockk.InternalPlatformDsl.toStr
import org.junit.Assert.*
import org.junit.Before

import org.junit.Test
import ua.blackwindstudio.cryptoexchangeapp.coin.data.db.model.CoinDbModel
import ua.blackwindstudio.cryptoexchangeapp.coin.data.network.model.*
import java.io.File

class CoinMapperTest {

    private lateinit var mapper: CoinMapper

    @Before
    fun setup() {
        mapper = CoinMapper()
    }

    @Test
    fun `coin data transfer object properly converts to db model`() {
        val testCoinInfoDto = CoinInfoDto(
            fromSymbol = "test from symbol",
            toSymbol = "test to symbol",
            price = "test price",
            lastUpdate = "test last update",
            highDay = "test high day",
            lowDay = "test low day",
            lastMarket = "test last market",
            imageUrl = "test image url"
        )

        val baseUrl = "https://www.cryptocompare.com/"

        val expectedDbModel = CoinDbModel(
            fromSymbol = "test from symbol",
            toSymbol = "test to symbol",
            price = "test price",
            lastUpdate = "test last update",
            highDay = "test high day",
            lowDay = "test low day",
            lastMarket = "test last market",
            imageUrl = baseUrl + "test image url"
        )

        val actualDbModel = mapper.mapDtoToDb(testCoinInfoDto)

        assertEquals(expectedDbModel, actualDbModel)
    }

    @Test
    fun `coin full price dto object correctly mapped to coin info list`() {
        val raw =
            ClassLoader.getSystemResource(COIN_PRICE_RAW_JSON_FILE_NAME).readText()

        val json = Gson().fromJson(raw, JsonObject::class.java)

        val dto = CoinsInfoContainerDto(
            json
        )
        val expected = listOf(
            CoinInfoDto(
                fromSymbol = "AAVE",
                toSymbol = "UAH",
                price = "3307.6476",
                lastUpdate = "1667916526",
                highDay = "3584.9235",
                lowDay = "3148.0526",
                lastMarket = "dcoin",
                imageUrl = "/media/37747534/aave.png"
            ),
            CoinInfoDto(
                fromSymbol = "ADA",
                toSymbol = "UAH",
                price = "15.72176",
                lastUpdate = "1667916516",
                highDay = "16.499835",
                lowDay = "15.082232",
                lastMarket = "Binance",
                imageUrl = "/media/37746235/ada.png"
            ),
            CoinInfoDto(
                fromSymbol = "ALGO",
                toSymbol = "UAH",
                price = "15.434068",
                lastUpdate = "1667916518",
                highDay = "17.05674",
                lowDay = "14.880382",
                lastMarket = "Binance",
                imageUrl = "/media/38553120/algo.png"
            )
        )

        val actual = mapper.mapExchangeInfoToListCoinInfo(dto)

        assertEquals(expected, actual)
    }

    @Test
    fun `coin full price throws IllegalArgumentException if rawData is empty`() {
        val expected = IllegalArgumentException()
        val dto = CoinsInfoContainerDto(null)

        assertThrows(expected::class.java) {
            mapper.mapExchangeInfoToListCoinInfo(dto)
        }

    }

    @Test
    fun `top coin list dto object correctly mapped to string`() {

        val dto = CoinNamesListDto(
            listOf(
                CoinNameContainerDto(
                    CoinNameDto(
                        "test_name_one",
                        "test_image_one"
                    )
                ),
                CoinNameContainerDto(
                    CoinNameDto(
                        "test_name_two",
                        "test_image_two"
                    )
                ),
                CoinNameContainerDto(
                    CoinNameDto(
                        "test_name_three",
                        "test_image_three"
                    )
                )
            )
        )
        val expected = "test_name_one,test_name_two,test_name_three"

        val actual = mapper.convertTopCoinsInfoDtoToString(dto)

        assertEquals(expected, actual)
    }

    companion object {
        private const val COIN_PRICE_RAW_JSON_FILE_NAME = "coin_price_raw_json.txt"
    }
}