package ua.blackwindstudio.cryptoexchangeapp.coin.ui.coin_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ua.blackwindstudio.cryptoexchangeapp.coin.data.CoinRepository
import ua.blackwindstudio.cryptoexchangeapp.coin.data.db.model.CoinDbModel
import ua.blackwindstudio.cryptoexchangeapp.coin.ui.model.UiCoin

@OptIn(ExperimentalCoroutinesApi::class)
class CoinListViewModel: ViewModel() {
    private val _coinList = MutableStateFlow<List<UiCoin>>(emptyList())
    val coinList: StateFlow<List<UiCoin>> = _coinList

    init {
        viewModelScope.launch {
            CoinRepository.initializeRepository(COIN_LIST_SIZE_LIMIT, ToSymbol.USD.name)
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

    fun changeToSymbol(position: Int) {
        CoinRepository.changeToSymbol(ToSymbol.values()[position].name)
    }

    enum class ToSymbol {
        USD, EUR, UAH,
    }

    companion object {
        private const val COIN_LIST_SIZE_LIMIT = 50
    }
}