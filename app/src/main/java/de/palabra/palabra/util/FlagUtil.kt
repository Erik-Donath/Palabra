package de.palabra.palabra.util

import de.palabra.palabra.R

object FlagUtil {
    val langCodes = listOf("en", "de", "fr", "es", "it", "ru", "zh", "pt", "nl", "pl", "sv")

    fun getFlagResForLang(langCode: String): Int {
        return when (langCode) {
            "en" -> R.drawable.flag_gb // UK flag, or use flag_us for US
            "de" -> R.drawable.flag_de
            "fr" -> R.drawable.flag_fr
            "es" -> R.drawable.flag_es
            "it" -> R.drawable.flag_it
            "ru" -> R.drawable.flag_ru
            "zh" -> R.drawable.flag_cn
            "pt" -> R.drawable.flag_pt
            "nl" -> R.drawable.flag_nl
            "pl" -> R.drawable.flag_pl
            "sv" -> R.drawable.flag_se
            // ...add more as needed
            else -> R.drawable.flag__unknown // fallback (add a generic flag if you want)
        }
    }
}