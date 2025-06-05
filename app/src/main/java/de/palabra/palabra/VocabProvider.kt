package de.palabra.palabra

import android.content.Context
import de.palabra.palabra.db.PalabraDatabase
import de.palabra.palabra.db.Vocab
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.Serializable

class VocabProvider(
    private val vocabs: List<Vocab>
) : Serializable {
    private var currentIndex: Int = 0
    private val progressMap: MutableMap<Int, Boolean?> = mutableMapOf()
    private val quizData: List<GuessData> = generateQuizData()

    data class GuessData(
        val vocab: Vocab,
        val vocabId: Int,
        val word: String,
        val solutions: List<String>,
        val correctIndex: Int
    ) : Serializable

    init {
        resetProgress()
    }

    private fun generateQuizData(): List<GuessData> {
        if (vocabs.isEmpty()) return emptyList()
        
        val shuffledVocabs = vocabs.shuffled()
        val allAnswers = vocabs.map { it.toWord }.distinct()
        
        return shuffledVocabs.map { vocab ->
            val correctAnswer = vocab.toWord
            val distractors = allAnswers
                .filter { it != correctAnswer }
                .shuffled()
                .take(2)
                .toMutableList()
            
            val correctIndex = (0..distractors.size).random()
            distractors.add(correctIndex, correctAnswer)
            
            GuessData(
                vocab = vocab,
                vocabId = vocab.id,
                word = vocab.word,
                solutions = distractors,
                correctIndex = correctIndex
            )
        }
    }

    fun getNextGuess(): GuessData? {
        return if (currentIndex < quizData.size) {
            quizData[currentIndex++]
        } else {
            null
        }
    }

    fun isEmpty(): Boolean = vocabs.isEmpty()
    fun isNotEmpty(): Boolean = vocabs.isNotEmpty()

    fun getProgressList(): List<Triple<Vocab, Boolean?, Int>> =
        vocabs.mapIndexed { index, vocab -> 
            Triple(vocab, progressMap[vocab.id], index) 
        }

    fun reset() {
        currentIndex = 0
        resetProgress()
    }

    private fun resetProgress() {
        progressMap.clear()
        vocabs.forEach { vocab ->
            progressMap[vocab.id] = null
        }
    }

    suspend fun submitResult(context: Context, vocabId: Int, isCorrect: Boolean) {
        progressMap[vocabId] = isCorrect
        
        withContext(Dispatchers.IO) {
            val database = PalabraDatabase.getInstance(context)
            if (isCorrect) {
                database.vocabDao().incrementCorrectCount(vocabId)
            } else {
                database.vocabDao().incrementWrongCount(vocabId)
            }
        }
    }

    fun getVocabById(id: Int): Vocab? = vocabs.find { it.id == id }
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
