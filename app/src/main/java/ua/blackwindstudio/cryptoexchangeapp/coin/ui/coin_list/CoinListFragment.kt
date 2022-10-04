package ua.blackwindstudio.cryptoexchangeapp.coin.ui.coin_list

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import ua.blackwindstudio.cryptoexchangeapp.R
import ua.blackwindstudio.cryptoexchangeapp.coin.ui.adapters.CoinListAdapter
import ua.blackwindstudio.cryptoexchangeapp.coin.ui.coin_details.CoinDetailsFragment
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
        setupSpinner()

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.coinList.collectLatest { list ->
                Log.d("FLOW_DEBUG", "Collecting first list:empty is ${list.isEmpty()}")
                updateRecyclerList(list)
            }
        }
        if (!inPortraitMode()
            && binding.detailFragmentContainer!!.childCount == 0
        ) {
            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                Log.d("FLOW_DEBUG", "launching collecting")
                viewModel.coinList.collectLatest { list ->
                    Log.d("FLOW_DEBUG", "Collecting second list:empty is ${list.isEmpty()}")
                    if (list.isNotEmpty()) {
                        initializeLandscapeMode(list.first())
                        cancel()
                    }
                }
            }
        }
    }

    private fun initializeLandscapeMode(coin: UiCoin) {
        with(childFragmentManager) {
            popBackStack()
            beginTransaction().replace(
                R.id.detail_fragment_container,
                CoinDetailsFragment.getInstance(coin)
            ).addToBackStack(null).commit()
        }
    }

    private fun setupSpinner() {
        binding.spinnerChooseCurrency.apply {
            adapter = ArrayAdapter.createFromResource(
                requireContext(), R.array.to_symbols,
                R.layout.item_to_symbol
            )
            onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    adapter: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    p3: Long
                ) {
                    viewModel.changeToSymbol(position)
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    //do nothing
                }
            }
        }
    }

    private fun setupRecycler() {
        binding.recyclerCoinList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = CoinListAdapter(
                CoinListAdapter.CoinClickListener { coin ->
                    navigateToDetailFragment(coin)
                }
            )
            itemAnimator = null
        }
    }

    private fun navigateToDetailFragment(coin: UiCoin) {
        //TODO deal with backStack
        if (inPortraitMode()) {
            logBackStack(parentFragmentManager, "entry")
            parentFragmentManager.beginTransaction().replace(
                R.id.fragment_root,
                CoinDetailsFragment.getInstance(coin)
            ).addToBackStack(null).commit()
            logBackStack(parentFragmentManager, "out")
        } else {
            logBackStack(childFragmentManager, "entry")
            childFragmentManager.popBackStack()
            childFragmentManager.beginTransaction().replace(
                R.id.detail_fragment_container,
                CoinDetailsFragment.getInstance(coin)
            ).addToBackStack(null).commit()
            logBackStack(childFragmentManager, "out")
        }
    }

    private fun logBackStack(manager: FragmentManager, m: String) {
        Log.d(
            "BACKSTACK_DEBUG",
            "$m ${manager.backStackEntryCount}"
        )
    }

    private fun inPortraitMode() = binding.detailFragmentContainer == null

    private fun updateRecyclerList(list: List<UiCoin>) {
        (binding.recyclerCoinList.adapter as CoinListAdapter).submitList(list)
    }
}