package ua.blackwindstudio.cryptoexchangeapp.coin.ui.coin_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ua.blackwindstudio.cryptoexchangeapp.App
import ua.blackwindstudio.cryptoexchangeapp.coin.data.CoinRepository
import ua.blackwindstudio.cryptoexchangeapp.coin.ui.mappers.Mapper
import ua.blackwindstudio.cryptoexchangeapp.coin.ui.model.UiCoin

@OptIn(ExperimentalCoroutinesApi::class)
class CoinDetailsViewModel(private val fromSymbol: String): ViewModel() {
    private val mapper = Mapper(App.INSTANCE!!.applicationContext)
    private val _coin = MutableStateFlow(
        UiCoin(
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
        )
    )
    val coin: StateFlow<UiCoin> = _coin

    init {
        viewModelScope.launch {
            CoinRepository.getCoinBySymbol(fromSymbol).collectLatest { dbModel ->
                _coin.update { mapper.mapDbModelToUiCoin(dbModel) }
            }
        }
    }
}