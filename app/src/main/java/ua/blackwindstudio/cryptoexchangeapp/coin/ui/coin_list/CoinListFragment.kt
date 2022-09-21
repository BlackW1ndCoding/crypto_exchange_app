package ua.blackwindstudio.cryptoexchangeapp.coin.ui.coin_list

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import ua.blackwindstudio.cryptoexchangeapp.R
import ua.blackwindstudio.cryptoexchangeapp.databinding.FragmentCoinListBinding

class CoinListFragment: Fragment(R.layout.fragment_coin_list) {

    private lateinit var binding: FragmentCoinListBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentCoinListBinding.bind(view)
    }
}