package de.palabra.palabra

import de.palabra.palabra.db.LektionWithVocabs
import de.palabra.palabra.db.Vocab

class LektionProvider(
    lektionWithVocab: LektionWithVocabs
) : IProvider {
    private val data: List<GuessData>
    private var index: Int = 0

    init {
        val vocabs: List<Vocab> = lektionWithVocab.vocabs
        require(vocabs.isNotEmpty()) { "LektionProvider needs at least one vocab" }

        val allSolutions = vocabs.map { it.toWord }.toSet()
        data = vocabs.map { vocab ->
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