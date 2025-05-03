package de.palabra.palabra

interface IProvider {
    fun next(): GuessData?
    fun all(): List<String>
}

class DebugProvider: IProvider {
    private val sampleData = listOf(
        GuessData("Hund", listOf("dog", "cat", "mouse"), 0),
        GuessData("Katze", listOf("bird", "cat", "horse"), 1),
        GuessData("Haus", listOf("car", "tree", "house"), 2)
    )
    private var index: Int = 0

    override fun next(): GuessData? {
        return sampleData.getOrNull(index++)
    }

    override fun all(): List<String> {
        return sampleData.map { "${it.word} -> ${it.solutions[it.correctIndex]}" }
    }
}