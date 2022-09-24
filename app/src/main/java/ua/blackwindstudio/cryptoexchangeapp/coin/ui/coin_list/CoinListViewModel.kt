package ua.blackwindstudio.cryptoexchangeapp.coin.ui.coin_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ua.blackwindstudio.cryptoexchangeapp.coin.data.CoinRepository
import ua.blackwindstudio.cryptoexchangeapp.coin.data.network.CoinApiFactory
import ua.blackwindstudio.cryptoexchangeapp.coin.ui.model.UiCoin

class CoinListViewModel: ViewModel() {
    private val _coinList = MutableStateFlow<List<UiCoin>>(emptyList())
    val coinList: StateFlow<List<UiCoin>> = _coinList

    init {
        viewModelScope.launch {
            CoinRepository.loadNewPricesIntoDB(COIN_LIST_SIZE_LIMIT, DEFAULT_CURRENCY)
        }
        viewModelScope.launch {
            CoinRepository.getPriceList().collectLatest {
                _coinList.update { list ->
                    list.map { coin ->
                        UiCoin(
                            fromSymbol = coin.fromSymbol,
                            toSymbol = coin.toSymbol,
                            price = coin.price,
                            imageUrl = coin.imageUrl
                        )
                    }
                }
            }
        }
    }

    companion object {
        private const val COIN_LIST_SIZE_LIMIT = 50
        private const val DEFAULT_CURRENCY = "USD"
    }
}