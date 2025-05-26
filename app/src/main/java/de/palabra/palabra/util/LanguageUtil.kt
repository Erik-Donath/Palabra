package de.palabra.palabra.util

import de.palabra.palabra.R

object LanguageUtil {
    data class Language(
        val name: String,
        val code: String,
        val flagRes: Int
    )

    private val supportedLanguages = listOf(
        Language("English (GB)",                "en",    R.drawable.flag_gb), // en-GB
        Language("Spanish",                     "es",    R.drawable.flag_es),
        Language("French",                      "fr",    R.drawable.flag_fr),
        Language("German",                      "de",    R.drawable.flag_de),
        Language("Italian",                     "it",    R.drawable.flag_it),
        Language("Portuguese (Brazilian)",      "pt-BR", R.drawable.flag_br),
        Language("Portuguese (Portugal)",       "pt",    R.drawable.flag_pt), // pt-PT
        Language("Dutch",                       "nl",    R.drawable.flag_nl),
        Language("Polish",                      "pl",    R.drawable.flag_pl),
        Language("Swedish",                     "sv",    R.drawable.flag_se),
        Language("Greek",                       "el",    R.drawable.flag_gr),
        Language("Czech",                       "cs",    R.drawable.flag_cz),
        Language("Hungarian",                   "hu",    R.drawable.flag_hu),
        Language("Romanian",                    "ro",    R.drawable.flag_ro),
        Language("Danish",                      "da",    R.drawable.flag_dk),
        Language("Norwegian",                   "no",    R.drawable.flag_no),
        Language("Finnish",                     "fi",    R.drawable.flag_fi),
        Language("English (US)",                "en-US", R.drawable.flag_us)
    )

    val languageNames = supportedLanguages.map { it.name }
    val languageCodes = supportedLanguages.map { it.code }

    fun getNameFromCode(code: String): String =
        supportedLanguages.find { it.code.equals(code, ignoreCase = true) }?.name ?: "Unknown"

    fun getFlagFromCode(code: String): Int =
        supportedLanguages.find { it.code.equals(code, ignoreCase = true) }?.flagRes ?: R.drawable.flag__unknown

    fun getCodeFromName(name: String): String =
        supportedLanguages.find { it.name.equals(name, ignoreCase = true) }?.code ?: ""
}