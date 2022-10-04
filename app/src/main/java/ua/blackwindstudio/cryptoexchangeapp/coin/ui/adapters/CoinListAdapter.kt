package ua.blackwindstudio.cryptoexchangeapp.coin.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ua.blackwindstudio.cryptoexchangeapp.R
import ua.blackwindstudio.cryptoexchangeapp.coin.ui.model.UiCoin
import ua.blackwindstudio.cryptoexchangeapp.coin.ui.utils.formatPrice
import ua.blackwindstudio.cryptoexchangeapp.databinding.ItemCoinBinding

class CoinListAdapter(private val coinClickListener: CoinClickListener):
    ListAdapter<UiCoin, CoinListAdapter.CoinListViewHolder>(DiffCallback) {

    inner class CoinListViewHolder(private val binding: ItemCoinBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(coin: UiCoin) {
            binding.apply {
                val context = binding.root.context

                textExchangePair.text = context.getString(
                    R.string.from_to,
                    coin.fromSymbol,
                    coin.toSymbol
                )
                textUpdatedAt.text = String.format(
                    context.getString(R.string.updated_at),
                    coin.updated
                )
                textPrice.text = formatPrice(coin.price)

                Glide.with(context)
                    .load(coin.imageUrl)
                    .into(coinImage)
                root.setOnClickListener { coinClickListener(coin) }
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

