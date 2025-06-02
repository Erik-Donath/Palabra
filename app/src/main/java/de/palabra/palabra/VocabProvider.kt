package de.palabra.palabra

import android.content.Context
import de.palabra.palabra.db.PalabraDatabase
import de.palabra.palabra.db.Vocab
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.Serializable
import kotlin.collections.shuffled

class VocabProvider(
    private val vocabs: List<Vocab>
) : Serializable {
    private var index: Int = 0
    private val progress: MutableMap<Int, Boolean?> = mutableMapOf()

    data class GuessData(
        val vocab: Vocab,
        val word: String,
        val solutions: List<String>,
        val correctIndex: Int
    ) : Serializable

    init {
        reset()
    }

    fun getNextGuess(): GuessData? {
        if (index >= vocabs.size) return null
        val vocab = vocabs[index]
        val allSolutions = vocabs.map { it.toWord }
        val distractors = allSolutions
            .filter { it != vocab.toWord }
            .shuffled()
            .take(2)
            .toMutableList()
        val correctIndex = (0..distractors.size).random()
        distractors.add(correctIndex, vocab.toWord)
        index++
        return GuessData(vocab, vocab.word, distractors, correctIndex)
    }

    fun isEmpty() = vocabs.isEmpty()
    fun isNotEmpty() = vocabs.isNotEmpty()

    fun getList(): List<Triple<Vocab, Boolean?, Int>> =
        vocabs.mapIndexed { i, vocab -> Triple(vocab, progress[vocab.id], i) }

    fun reset() {
        index = 0
        vocabs.shuffled()
        progress.replaceAll { _, _ -> null }
    }

    suspend fun submitResult(context: Context, vocab: Vocab, isCorrect: Boolean) {
        progress[vocab.id] = isCorrect
        val db = PalabraDatabase.getInstance(context)
        if (isCorrect) {
            db.vocabDao().incrementCorrectCount(vocab.id)
        } else {
            db.vocabDao().incrementWrongCount(vocab.id)
        }
    }

    fun findVocabByWord(word: String): Vocab? = vocabs.find { it.word == word }
}

suspend fun AllProviderFunction(context: Context): List<Vocab> = withContext(Dispatchers.IO) {
    PalabraDatabase.getInstance(context).vocabDao().getAllVocabs()
}

suspend fun LektionProviderFunction(context: Context, lektionId: Int): List<Vocab> = withContext(Dispatchers.IO) {
    PalabraDatabase.getInstance(context).vocabDao().getVocabsForLektion(lektionId)
}

suspend fun SmartProviderFunction(context: Context): List<Vocab> = withContext(Dispatchers.IO) {
    PalabraDatabase.getInstance(context).vocabDao()
        .getAllVocabs()
        .sortedByDescending {
            val total = it.correctCount + it.wrongCount
            if (total == 0) 0.0 else it.wrongCount.toDouble() / total
        }
        .take(25)
}