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

class DebugProvider : IProvider {
    private val guesses = listOf(
        GuessData(
            word = "Hund",
            solutions = listOf("Dog", "Cat", "Mouse"),
            correctIndex = 0
        ),
        GuessData(
            word = "Katze",
            solutions = listOf("Dog", "Cat", "Horse"),
            correctIndex = 1
        ),
        GuessData(
            word = "Vogel",
            solutions = listOf("Bird", "Fish", "Snake"),
            correctIndex = 0
        )
    )
    private var index = 0

    override fun getGuessList() = guesses

    override fun getNextGuess(): GuessData? = guesses.getOrNull(index++)

    override fun reset() { index = 0 }
}