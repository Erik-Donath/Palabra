package de.palabra.palabra

import de.palabra.palabra.db.LektionWithVocabs

class AllProvider(
    allLektions: List<LektionWithVocabs>
) : IProvider {
    private val data: List<GuessData>
    private var index: Int = 0

    init {
        val allVocabs = allLektions.flatMap { it.vocabs }
        require(allVocabs.isNotEmpty()) { "AllProvider needs at least one vocab in all lektions." }
        val allSolutions = allVocabs.map { it.toWord }.toSet()
        data = allVocabs.map { vocab ->
            val correctSolution = vocab.toWord
            val distractors = allSolutions
                .filter { it != correctSolution }
                .shuffled()
                .take(3)
                .toMutableList()
            val insertIndex = (0..distractors.size).random()
            distractors.add(insertIndex, correctSolution)
            GuessData(vocab.word, distractors, insertIndex)
        }
    }

    override fun getGuessList(): List<GuessData> = data

    override fun getNextGuess(): GuessData? = data.getOrNull(index++)

    override fun reset() { index = 0 }
}