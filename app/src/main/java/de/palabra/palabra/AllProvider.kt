package de.palabra.palabra

import android.content.Context
import de.palabra.palabra.db.PalabraDatabase
import de.palabra.palabra.db.Vocab
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.Serializable

class AllProvider private constructor(
    allVocabs: List<Vocab>
) : IProvider, Serializable {

    private val data: List<GuessData>
    private var index: Int = 0

    init {
        require(allVocabs.isNotEmpty()) { "AllProvider needs at least one vocab." }
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

    companion object {
        suspend fun create(context: Context): AllProvider? = withContext(Dispatchers.IO) {
            val db = PalabraDatabase.getInstance(context)
            val allVocabs = db.vocabDao().getAllVocabs()
            if (allVocabs.isNotEmpty()) AllProvider(allVocabs) else null
        }
    }
}