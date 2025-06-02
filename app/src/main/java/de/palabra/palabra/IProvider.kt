package de.palabra.palabra

import java.io.Serializable

data class GuessData(
    val word: String,
    val solutions: List<String>,
    val correctIndex: Int
) : Serializable

fun createGuessData(
    word: String,
    correctSolution: String,
    allSolutions: Set<String>
): GuessData? {
    val distractors = allSolutions
        .filter { it != correctSolution }
        .shuffled()
        .take(2)
        .toMutableList()

    val insertIndex = (0..2).random()
    distractors.add(insertIndex, correctSolution)
    return GuessData(word, distractors, insertIndex)
}

interface IProvider : Serializable {
    fun getGuessList(): List<GuessData>
    fun getNextGuess(): GuessData?
    fun reset()
}
