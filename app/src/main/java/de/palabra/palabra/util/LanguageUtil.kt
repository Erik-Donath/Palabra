package de.palabra.palabra.util

import de.palabra.palabra.R

object LanguageUtil {
    data class Language(
        val name: String,
        val code: String,
        val flagRes: Int // Replace with String if you use filenames
    )

    val supportedLanguages = listOf(
        Language("Englisch (GB)",               "en",    R.drawable.flag_gb), // en-GB
        Language("Englisch (US)",               "en-US", R.drawable.flag_us),
        Language("Spanish",                     "es",    R.drawable.flag_es),
        Language("French",                      "fr",    R.drawable.flag_fr),
        Language("German",                      "de",    R.drawable.flag_de),
        Language("Italian",                     "it",    R.drawable.flag_it),
        Language("Russian",                     "ru",    R.drawable.flag_ru),
        Language("Chinese",                     "zh",    R.drawable.flag_cn),
        Language("Japanese",                    "ja",    R.drawable.flag_jp),
        Language("Korean",                      "ko",    R.drawable.flag_kr),
        Language("Turkish",                     "tr",    R.drawable.flag_tr),
        Language("Dutch",                       "nl",    R.drawable.flag_nl),
        Language("Swedish",                     "sv",    R.drawable.flag_se),
        Language("Danish",                      "da",    R.drawable.flag_dk),
        Language("Norwegian",                   "no",    R.drawable.flag_no),
        Language("Finnish",                     "fi",    R.drawable.flag_fi),
        Language("Portuguese (Portugal)",       "pt",    R.drawable.flag_pt), // pt-PT
        Language("Portuguese (Brazilian)",      "pt-BR", R.drawable.flag_br),
        Language("Polish",                      "pl",    R.drawable.flag_pl),
        Language("Greek",                       "el",    R.drawable.flag_gr),
        Language("Hungarian",                   "hu",    R.drawable.flag_hu),
        Language("Czech",                       "cs",    R.drawable.flag_cz),
        Language("Romanian",                    "ro",    R.drawable.flag_ro),
        Language("Thai",                        "th",    R.drawable.flag_th),
        Language("Arabic (Saudi Arabia)",       "ar",    R.drawable.flag_sa), // ar-SA
        Language("Arabic (Egypt)",              "ar-EG", R.drawable.flag_eg),
        Language("Hindi",                       "hi",    R.drawable.flag_in),
        Language("Hebrew",                      "he",    R.drawable.flag_il),
        Language("Indonesian",                  "id",    R.drawable.flag_id),
        Language("Vietnamese",                  "vi",    R.drawable.flag_vn)
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