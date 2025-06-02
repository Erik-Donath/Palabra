package de.palabra.palabra

import android.content.Context
import de.palabra.palabra.db.LektionWithVocabs
import de.palabra.palabra.db.PalabraDatabase
import de.palabra.palabra.db.Vocab
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.Serializable

class LektionProvider private constructor(
    lektionWithVocab: LektionWithVocabs
) : IProvider, Serializable {

    private val data: List<GuessData>
    private var index: Int = 0

    init {
        val vocabs: List<Vocab> = lektionWithVocab.vocabs
        require(vocabs.isNotEmpty()) { "LektionProvider needs at least one vocab" }

        val allSolutions = vocabs.map { it.toWord }.toSet()
        data = vocabs.mapNotNull { vocab ->
            createGuessData(vocab.word, vocab.toWord, allSolutions)
        }
    }

    override fun getGuessList(): List<GuessData> = data

    override fun getNextGuess(): GuessData? = data.getOrNull(index++)

    override fun reset() { index = 0 }

    companion object {
        suspend fun create(context: Context, lektionId: Int): LektionProvider? = withContext(Dispatchers.IO) {
            val db = PalabraDatabase.getInstance(context)
            val lektionWithVocab = db.lektionDao().getLektionWithVocabs(lektionId)
            lektionWithVocab?.let { LektionProvider(it) }
        }
    }
}