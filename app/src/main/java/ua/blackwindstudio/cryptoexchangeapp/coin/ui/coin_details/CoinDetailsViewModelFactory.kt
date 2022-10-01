package ua.blackwindstudio.cryptoexchangeapp.coin.ui.coin_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Suppress("UNCHECKED_CAST")
class CoinDetailsViewModelFactory(private val fromSymbol: String): ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        return CoinDetailsViewModel(fromSymbol) as T
    }
}