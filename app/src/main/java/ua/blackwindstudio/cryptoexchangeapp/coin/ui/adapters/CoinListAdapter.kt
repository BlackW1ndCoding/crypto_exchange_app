package ua.blackwindstudio.cryptoexchangeapp.coin.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import ua.blackwindstudio.cryptoexchangeapp.R
import ua.blackwindstudio.cryptoexchangeapp.coin.ui.model.UiCoin
import ua.blackwindstudio.cryptoexchangeapp.databinding.ItemCoinBinding
import java.time.format.DateTimeFormatter

class CoinListAdapter(private val coinClickListener: CoinClickListener):
    ListAdapter<UiCoin, CoinListAdapter.CoinListViewHolder>(DiffCallback) {

    inner class CoinListViewHolder(private val binding: ItemCoinBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(coin: UiCoin) {
            binding.apply {
                textExchangePair.text = formExchangePair(coin)
                textPrice.text = formatPrice(coin.price)
                textUpdatedAt.text = String.format(
                    textUpdatedAt.context.getString(R.string.updated_at),
                    formatDate(coin.updated, textUpdatedAt.context)
                )
                Glide.with(binding.root)
                    .load(coin.imageUrl)
                    .into(coinImage)
                root.setOnClickListener { coinClickListener.click(coin) }
            }
        }

        private fun formExchangePair(coin: UiCoin): String {
            return "${coin.fromSymbol}/${coin.toSymbol}"
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

    private fun formatPrice(price: String): String {
        return try {
            String.format("%.4f", price.toFloat())
        } catch (e: Exception) {
            "#ERROR"
        }
    }

    private fun formatDate(updated: String, context: Context): String {
        return try {
            val epochTime = Instant.fromEpochSeconds(updated.toLong())
            val localTime =
                epochTime.toLocalDateTime(TimeZone.currentSystemDefault()).toJavaLocalDateTime()
            localTime.format(
                DateTimeFormatter.ofPattern(context.getString(R.string.date_time_pattern))
            )
        } catch (e: Exception) {
            "#ERROR"
        }
    }

    class CoinClickListener(private val onClick: (UiCoin) -> Unit) {
        fun click(coin: UiCoin) {
            onClick(coin)
        }
    }
}

