package ua.blackwindstudio.cryptoexchangeapp.coin.ui.coin_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ua.blackwindstudio.cryptoexchangeapp.coin.data.CoinRepository
import ua.blackwindstudio.cryptoexchangeapp.coin.ui.mappers.Mapper
import ua.blackwindstudio.cryptoexchangeapp.coin.ui.model.UiCoin

class CoinDetailsViewModel @AssistedInject constructor(
    @Assisted private val fromSymbol: String,
    mapper: Mapper,
    coinRepository: CoinRepository
):
    ViewModel() {
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
            coinRepository.getCoinBySymbol(fromSymbol).collectLatest { dbModel ->
                _coin.update { mapper.mapDbModelToUiCoin(dbModel) }
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(@Assisted fromSymbol: String): CoinDetailsViewModel
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideFactory(
            assistedFactory: Factory,
            fromSymbol: String
        ) = object: ViewModelProvider.Factory {
            override fun <T: ViewModel> create(modelClass: Class<T>): T {
                return assistedFactory.create(fromSymbol) as T
            }
        }
    }
}