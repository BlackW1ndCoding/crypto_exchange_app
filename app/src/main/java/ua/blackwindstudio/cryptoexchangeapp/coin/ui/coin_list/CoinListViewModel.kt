package ua.blackwindstudio.cryptoexchangeapp.coin.ui.coin_list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ua.blackwindstudio.cryptoexchangeapp.coin.data.CoinRepository
import ua.blackwindstudio.cryptoexchangeapp.coin.data.db.model.CoinDbModel
import ua.blackwindstudio.cryptoexchangeapp.coin.ui.model.UiCoin

class CoinListViewModel: ViewModel() {
    private val _coinList = MutableStateFlow<List<UiCoin>>(emptyList())
    val coinList: StateFlow<List<UiCoin>> = _coinList

    init {
        viewModelScope.launch {
            CoinRepository.loadData(COIN_LIST_SIZE_LIMIT, DEFAULT_CURRENCY)
        }

        viewModelScope.launch {
            CoinRepository.getPriceList().collectLatest { list ->
                _coinList.update {
                    list.map { coin: CoinDbModel ->
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