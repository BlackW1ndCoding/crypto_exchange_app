package ua.blackwindstudio.cryptoexchangeapp.coin.data.mapper

import org.junit.Assert.*
import org.junit.Before

import org.junit.Test
import ua.blackwindstudio.cryptoexchangeapp.coin.data.db.model.CoinDbModel
import ua.blackwindstudio.cryptoexchangeapp.coin.data.network.model.CoinInfoDto

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

        val expectedDbModel = CoinDbModel(
            fromSymbol = "test from symbol",
            toSymbol = "test to symbol",
            price = "test price",
            lastUpdate = "test last update",
            highDay = "test high day",
            lowDay = "test low day",
            lastMarket = "test last market",
            imageUrl = "test image url"
        )

        val actualDbModel = mapper.mapDtoToDb(testCoinInfoDto)

        assertEquals(expectedDbModel, actualDbModel)
    }

    @Test
    fun mapExchangeInfoToListCoinInfo() {
    }

    @Test
    fun convertTopCoinsInfoDtoToString() {
    }
}