package ua.blackwindstudio.cryptoexchangeapp.coin.ui.coin_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ua.blackwindstudio.cryptoexchangeapp.coin.data.CoinRepository
import ua.blackwindstudio.cryptoexchangeapp.coin.ui.mappers.Mapper
import javax.inject.Inject

class CoinListViewModelFactory @Inject constructor(
    private val repository: CoinRepository,
    private val mapper: Mapper
): ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        return CoinListViewModel(repository, mapper) as T
    }
}