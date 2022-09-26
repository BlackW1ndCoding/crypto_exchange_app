package ua.blackwindstudio.cryptoexchangeapp.coin.ui.coin_list

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.flow.collectLatest
import ua.blackwindstudio.cryptoexchangeapp.R
import ua.blackwindstudio.cryptoexchangeapp.coin.data.CoinRepository
import ua.blackwindstudio.cryptoexchangeapp.coin.ui.adapters.CoinListAdapter
import ua.blackwindstudio.cryptoexchangeapp.coin.ui.model.UiCoin
import ua.blackwindstudio.cryptoexchangeapp.coin.ui.utils.AutoClearedValue
import ua.blackwindstudio.cryptoexchangeapp.databinding.FragmentCoinListBinding

class CoinListFragment: Fragment(R.layout.fragment_coin_list) {

    private var binding by AutoClearedValue<FragmentCoinListBinding>(this)
    private val viewModel by viewModels<CoinListViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCoinListBinding.bind(view)
        setupRecycler()

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.coinList.collectLatest {
                Log.d("VIEWMODEL_DEBUG","Updating in fragment")
                updateRecyclerList(it)
            }
        }
    }

    private fun setupRecycler() {
        binding.recyclerCoinList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = CoinListAdapter()
        }
    }

    private fun updateRecyclerList(list: List<UiCoin>) {
        (binding.recyclerCoinList.adapter as CoinListAdapter).submitList(list)
    }
}