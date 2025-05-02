package de.palabra.palabra

import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class GuessData(
    val word: String,
    val solutions: List<String>,
    val correctIndex: Int
) : Parcelable
