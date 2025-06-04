package de.palabra.palabra.util

object FileFormatV1 {
    const val FORMAT_VERSION = 1

    data class Vocab(
        val word: String,
        val toWord: String,
        val correctCount: Int,
        val wrongCount: Int
    )

    data class Lektion(
        val formatVersion: Int = FORMAT_VERSION,
        val title: String,
        val fromLangCode: String,
        val toLangCode: String,
        val description: String?,
        val vocabs: List<Vocab>
    )
}