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
import ua.blackwindstudio.cryptoexchangeapp.coin.ui.model.UiToSymbol

class CoinListViewModel @AssistedInject constructor(
    private val repository: CoinRepository,
    private val mapper: Mapper
):
    ViewModel() {
    private val _coinList = MutableStateFlow<List<UiCoin>>(emptyList())
    val coinList: StateFlow<List<UiCoin>> = _coinList

    private val _toSymbol = MutableStateFlow(UiToSymbol.USD)
    val toSymbol = _toSymbol as StateFlow<UiToSymbol>


    val errorsChannel = repository.errorsChannel

    init {
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

        viewModelScope.launch {
            repository.getToSymbol().collectLatest { dbModel ->
                _toSymbol.update {
                    try {
                        mapper.mapToSymbol(dbModel)
                    } catch (e: Exception) {
                        UiToSymbol.USD
                    }
                }
            }
        }
    }

    fun changeToSymbol(position: Int) {
        if (_toSymbol.value.ordinal != position) {
            repository.changeToSymbol(UiToSymbol.values()[position].name)
        }
    }

    companion object {
        private const val COIN_LIST_SIZE_LIMIT = 50
    }
}