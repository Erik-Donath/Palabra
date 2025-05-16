package de.palabra.palabra.util

import android.content.Context

object FlagUtils {
    fun getFlagResId(context: Context, countryCode: String): Int {
        // Android-Flags uses 2-letter lowercase country codes, e.g., flag_de, flag_us, flag_fr
        return context.resources.getIdentifier(
            "flag_${countryCode.lowercase()}",
            "drawable",
            context.packageName
        )
    }
}