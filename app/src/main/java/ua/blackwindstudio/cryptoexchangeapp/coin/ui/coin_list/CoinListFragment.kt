package ua.blackwindstudio.cryptoexchangeapp.coin.ui.coin_list

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import ua.blackwindstudio.cryptoexchangeapp.R
import ua.blackwindstudio.cryptoexchangeapp.appComponent
import ua.blackwindstudio.cryptoexchangeapp.coin.ui.adapters.CoinListAdapter
import ua.blackwindstudio.cryptoexchangeapp.coin.ui.coin_details.CoinDetailsFragment
import ua.blackwindstudio.cryptoexchangeapp.coin.ui.model.UiCoin
import ua.blackwindstudio.cryptoexchangeapp.coin.ui.utils.AutoClearedValue
import ua.blackwindstudio.cryptoexchangeapp.databinding.FragmentCoinListBinding
import javax.inject.Inject

class CoinListFragment: Fragment(R.layout.fragment_coin_list) {

    private var binding by AutoClearedValue<FragmentCoinListBinding>(this)

    @Inject
    lateinit var coinListAdapter: CoinListAdapter

    @Inject
    lateinit var viewModelFactory: CoinListViewModelFactory
    private val viewModel by viewModels<CoinListViewModel> {
        viewModelFactory
    }

    override fun onAttach(context: Context) {
        context.appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCoinListBinding.bind(view)
        setupRecycler()
        setupSpinner()

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.coinList.collectLatest { list ->
                updateRecyclerList(list)
            }
        }
        if (!inPortraitMode()
            && binding.detailFragmentContainer!!.childCount == 0
        ) {
            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                viewModel.coinList.collectLatest { list ->
                    if (list.isNotEmpty()) {
                        initializeLandscapeMode(list.first())
                        cancel()
                    }
                }
            }
        }
    }

    private fun initializeLandscapeMode(coin: UiCoin) {
        with(parentFragmentManager) {
            popBackStack()
            beginTransaction().replace(
                R.id.detail_fragment_container,
                CoinDetailsFragment.getInstance(coin)
            ).commit()
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
            adapter = coinListAdapter.apply {
                coinClickListener = CoinListAdapter.CoinClickListener { coin ->
                    navigateToDetailFragment(coin)
                }
            }
            itemAnimator = null
        }

    }

    private fun navigateToDetailFragment(coin: UiCoin) {
        if (inPortraitMode()) {
            parentFragmentManager.beginTransaction().replace(
                R.id.fragment_root,
                CoinDetailsFragment.getInstance(coin)
            ).addToBackStack(null).commit()
        } else {
            parentFragmentManager.popBackStack()
            parentFragmentManager.beginTransaction().replace(
                R.id.detail_fragment_container,
                CoinDetailsFragment.getInstance(coin)
            ).commit()
        }
    }

    private fun inPortraitMode() = requireContext().resources.getBoolean(R.bool.isPortrait)

    private fun updateRecyclerList(list: List<UiCoin>) {
        (binding.recyclerCoinList.adapter as CoinListAdapter).submitList(list)
    }

}