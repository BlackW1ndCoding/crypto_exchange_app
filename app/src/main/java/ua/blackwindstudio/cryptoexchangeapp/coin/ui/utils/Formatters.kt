package ua.blackwindstudio.cryptoexchangeapp.coin.ui.utils

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun formatPrice(price: String): String {
    return try {
        String.format("%.4f", price.toFloat())
    } catch (e: Exception) {
        "#ERROR"
    }
}

fun formatDate(updated: String, pattern: String): String {
    return try {
        val epochTime = Instant.fromEpochSeconds(updated.toLong())

        val localTime = epochTime.toLocalDateTime(TimeZone.currentSystemDefault())
        String.format(
            pattern,
            localTime.dayOfMonth,
            localTime.monthNumber,
            localTime.time.toString()
        )
    } catch (e: Exception) {
        "#ERROR"
    }
}