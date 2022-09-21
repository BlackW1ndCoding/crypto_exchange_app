package ua.blackwindstudio.cryptoexchangeapp.coin.ui.coin_list

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import ua.blackwindstudio.cryptoexchangeapp.R
import ua.blackwindstudio.cryptoexchangeapp.coin.ui.adapters.CoinListAdapter
import ua.blackwindstudio.cryptoexchangeapp.coin.ui.model.UiCoin
import ua.blackwindstudio.cryptoexchangeapp.databinding.FragmentCoinListBinding

class CoinListFragment: Fragment(R.layout.fragment_coin_list) {

    private lateinit var binding: FragmentCoinListBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentCoinListBinding.bind(view)

        setupRecycler()
    }

    val list = listOf<UiCoin>(
        UiCoin(0, "ETH", "USD", 600.00f, ""),
        UiCoin(1, "BTC", "USD", 1700.00f, "https://www.freecodecamp.org/news/content/images/size/w60/2021/05/tomer-ben-rachel-gravatar.jpeg"),
        UiCoin(2, "ZRK", "USD", 20.01f, ""),
        UiCoin(3, "MRAW", "USD", 20.40f, "")
    )

    private fun setupRecycler() {
        binding.recyclerCoinList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = CoinListAdapter()
            (adapter as CoinListAdapter).submitList(list)
        }
    }
}