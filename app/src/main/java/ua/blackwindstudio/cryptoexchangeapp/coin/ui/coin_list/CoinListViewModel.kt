package ua.blackwindstudio.cryptoexchangeapp.coin.ui.coin_list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ua.blackwindstudio.cryptoexchangeapp.coin.data.CoinRepository
import ua.blackwindstudio.cryptoexchangeapp.coin.data.db.model.CoinDbModel
import ua.blackwindstudio.cryptoexchangeapp.coin.ui.mappers.Mapper
import ua.blackwindstudio.cryptoexchangeapp.coin.ui.model.UiCoin

class CoinListViewModel @AssistedInject constructor(
    private val repository: CoinRepository,
    private val mapper: Mapper
):
    ViewModel() {
    private val _coinList = MutableStateFlow<List<UiCoin>>(emptyList())
    val coinList: StateFlow<List<UiCoin>> = _coinList

    init {
        viewModelScope.launch {
            repository.updatePriceList("USD", 10000L)
        }

        viewModelScope.launch {
            repository.getPriceList().collectLatest { list ->
                Log.d("REPOSITORY", "Collecting in VM")
                _coinList.update {
                    list.map { dbModel: CoinDbModel ->
                        mapper.mapDbModelToUiCoin(dbModel)
                    }
                }
            }
        }
    }

    fun changeToSymbol(position: Int) {
        viewModelScope.launch {
            repository.changeToSymbol(ToSymbol.values()[position].name)
        }
    }

    enum class ToSymbol {
        USD, EUR, UAH,
    }

    companion object {
        private const val COIN_LIST_SIZE_LIMIT = 50
    }
}