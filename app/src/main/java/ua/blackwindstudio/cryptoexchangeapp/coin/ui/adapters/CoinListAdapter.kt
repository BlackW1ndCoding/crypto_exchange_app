package ua.blackwindstudio.cryptoexchangeapp.coin.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ua.blackwindstudio.cryptoexchangeapp.coin.ui.ResourceProvider
import ua.blackwindstudio.cryptoexchangeapp.coin.ui.model.UiCoin
import ua.blackwindstudio.cryptoexchangeapp.coin.ui.utils.formatPrice
import ua.blackwindstudio.cryptoexchangeapp.databinding.ItemCoinBinding
import javax.inject.Inject
import ua.blackwindstudio.cryptoexchangeapp.coin.ui.utils.ERROR_STRING_TEMPLATE
import ua.blackwindstudio.cryptoexchangeapp.coin.ui.utils.FROM_TO_STRING_NAME
import ua.blackwindstudio.cryptoexchangeapp.coin.ui.utils.UPDATED_AT_STRING_NAME

class CoinListAdapter @Inject constructor(
    private val resourceProvider: ResourceProvider<String>
):
    ListAdapter<UiCoin, CoinListAdapter.CoinListViewHolder>(DiffCallback) {

    var coinClickListener: CoinClickListener? = null

    inner class CoinListViewHolder(private val binding: ItemCoinBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(coin: UiCoin) {
            val strings = resourceProvider.resourcesMap

            binding.apply {
                val context = binding.root.context

                textExchangePair.text = String.format(
                    strings[FROM_TO_STRING_NAME] ?: ERROR_STRING_TEMPLATE,
                    coin.fromSymbol,
                    coin.toSymbol
                )
                textUpdatedAt.text = String.format(
                    strings[UPDATED_AT_STRING_NAME] ?: ERROR_STRING_TEMPLATE,
                    coin.updated
                )
                textPrice.text = formatPrice(coin.price)

                Glide.with(context)
                    .load(coin.imageUrl)
                    .into(coinImage)
                root.setOnClickListener { coinClickListener?.invoke(coin) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return CoinListViewHolder(
            ItemCoinBinding.inflate(inflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CoinListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    object DiffCallback: DiffUtil.ItemCallback<UiCoin>() {
        override fun areItemsTheSame(oldItem: UiCoin, newItem: UiCoin): Boolean {
            return oldItem.fromSymbol == newItem.fromSymbol
        }

        override fun areContentsTheSame(oldItem: UiCoin, newItem: UiCoin): Boolean {
            return oldItem == newItem
        }
    }

    class CoinClickListener(private val onClick: (UiCoin) -> Unit) {
        operator fun invoke(coin: UiCoin) {
            onClick(coin)
        }
    }
}

