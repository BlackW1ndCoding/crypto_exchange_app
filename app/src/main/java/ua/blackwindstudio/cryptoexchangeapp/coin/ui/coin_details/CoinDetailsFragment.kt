package ua.blackwindstudio.cryptoexchangeapp.coin.ui.coin_details

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import kotlinx.coroutines.flow.collectLatest
import ua.blackwindstudio.cryptoexchangeapp.R
import ua.blackwindstudio.cryptoexchangeapp.coin.ui.model.UiCoin
import ua.blackwindstudio.cryptoexchangeapp.coin.ui.utils.autoCleared
import ua.blackwindstudio.cryptoexchangeapp.databinding.FragmentCoinDetailsBinding
import ua.blackwindstudio.cryptoexchangeapp.databinding.FragmentCoinListBinding

class CoinDetailsFragment: Fragment(R.layout.fragment_coin_details) {
    private var binding by autoCleared<FragmentCoinDetailsBinding>()
    private lateinit var viewModel: CoinDetailsViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args =
            arguments?.getString(COIN_ARGUMENT) ?: throw Exception("Argument not provided!!")
        viewModel = CoinDetailsViewModelFactory(args).create(CoinDetailsViewModel::class.java)

        binding = FragmentCoinDetailsBinding.bind(view)

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.coin.collectLatest { coin ->
                bindCoinInformation(coin)
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback {
            parentFragmentManager
                .popBackStack()
        }
    }

    private fun bindCoinInformation(coin: UiCoin) {
        binding.apply {
            textFromTo.text =
                getString(
                    R.string.from_to,
                    coin.fromSymbol, coin.toSymbol
                )
            textPrice.text = getString(R.string.price, coin.price)
            textDailyMin.text = getString(R.string.daily_min, coin.dailyMin)
            textDailyMax.text = getString(R.string.daily_max, coin.dailyMax)
            textLastMarket.text = getString(R.string.last_market, coin.lastMarket)
            textUpdatedAt.text = getString(R.string.updated_at, coin.updated)
            Glide.with(requireContext())
                .load(coin.imageUrl)
                .into(coinImage)
        }
    }

    companion object {
        const val COIN_ARGUMENT = "COIN_ARGUMENT"

        fun getInstance(coin: UiCoin): CoinDetailsFragment {
            return CoinDetailsFragment().apply {
                arguments = Bundle().apply {
                    putString(COIN_ARGUMENT, coin.fromSymbol)
                }
            }
        }
    }
}