package ua.blackwindstudio.cryptoexchangeapp.coin.ui.utils

import android.content.Context
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import ua.blackwindstudio.cryptoexchangeapp.R

fun formatPrice(price: String): String {
    return try {
        String.format("%.4f", price.toFloat())
    } catch (e: Exception) {
        "#ERROR"
    }
}

fun formatDate(updated: String, context: Context): String {
    return try {
        val epochTime = Instant.fromEpochSeconds(updated.toLong())

        val localTime = epochTime.toLocalDateTime(TimeZone.currentSystemDefault())
        String.format(
            context.getString(R.string.date_time_pattern),
            localTime.dayOfMonth,
            localTime.monthNumber,
            localTime.time.toString()
        )
    } catch (e: Exception) {
        "#ERROR"
    }
}