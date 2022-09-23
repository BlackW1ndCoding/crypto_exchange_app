package ua.blackwindstudio.cryptoexchangeapp.coin.ui.coin_list

import androidx.lifecycle.Transformations.map
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ua.blackwindstudio.cryptoexchangeapp.coin.data.CoinRepository
import ua.blackwindstudio.cryptoexchangeapp.coin.data.api.CoinApiFactory
import ua.blackwindstudio.cryptoexchangeapp.coin.ui.model.UiCoin

class CoinListViewModel: ViewModel() {
    private val _coinList = MutableStateFlow<List<UiCoin>>(emptyList())
    val coinList: StateFlow<List<UiCoin>> = _coinList

    init {
        viewModelScope.launch {
            _coinList.update {
                CoinRepository.getTopCoins(COIN_LIST_SIZE_LIMIT).coinFullData?.map { fullData ->
                    val coinInfo = fullData.coinInfo ?: return@update emptyList()
                    UiCoin(
                        0,
                        coinInfo.name ?: "Error",
                        "",
                        0f,
                        CoinApiFactory.BASE_IMAGE_URL + coinInfo.imageUrl
                    )
                } ?: emptyList()
            }
        }
    }

    companion object {
        private const val COIN_LIST_SIZE_LIMIT = 50
    }
}