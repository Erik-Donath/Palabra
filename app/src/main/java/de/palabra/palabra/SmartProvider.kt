package de.palabra.palabra

class SmartProvider : IProvider {
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