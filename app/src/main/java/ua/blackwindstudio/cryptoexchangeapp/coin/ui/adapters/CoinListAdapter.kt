package ua.blackwindstudio.cryptoexchangeapp.coin.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ua.blackwindstudio.cryptoexchangeapp.coin.ui.model.UiCoin
import ua.blackwindstudio.cryptoexchangeapp.databinding.ItemCoinBinding

class CoinListAdapter:
    ListAdapter<UiCoin, CoinListAdapter.CoinListViewHolder>(DiffCallback) {

    inner class CoinListViewHolder(private val binding: ItemCoinBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(coin: UiCoin) {
            binding.apply {
                textExchangePair.text = formExchangePair(coin)
                textPrice.text = coin.price.toString()
                Glide.with(binding.root)
                    .load(coin.imageUrl)
                    .into(coinImage)

            }
        }

        private fun formExchangePair(coin: UiCoin): String {
            return "${coin.coin}/${coin.currency}"
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
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: UiCoin, newItem: UiCoin): Boolean {
            return oldItem == newItem
        }
    }
}