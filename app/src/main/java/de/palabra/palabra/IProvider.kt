package de.palabra.palabra

import java.io.Serializable

data class GuessData(
    val word: String,
    val solutions: List<String>,
    val correctIndex: Int
) : Serializable

interface IProvider : Serializable {
    fun getGuessList(): List<GuessData>
    fun getNextGuess(): GuessData?
    fun reset()
}
