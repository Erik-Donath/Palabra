package de.palabra.palabra

import de.palabra.palabra.db.AppDatabase
import de.palabra.palabra.db.Lektion
import de.palabra.palabra.db.Vocab
import kotlinx.coroutines.flow.Flow

class PalabraRepository private constructor() {
    companion object {
        @Volatile
        private var INSTANCE: PalabraRepository? = null

        fun getRepo(): PalabraRepository {
            return INSTANCE ?: synchronized(this) {
                PalabraRepository().apply {
                    INSTANCE = this
                }
            }
        }
    }

    private val lektionDao = AppDatabase.getDatabase().lektionDao()
    private val vocabDao = AppDatabase.getDatabase().vocabDao()

    // Lektionen
    val allLektions: Flow<List<Lektion>> = lektionDao.getAllLektions()

    suspend fun insertLektion(lektion: Lektion) = lektionDao.insertLektion(lektion)

    suspend fun updateLektion(lektion: Lektion) = lektionDao.updateLektion(lektion)

    suspend fun deleteLektion(lektion: Lektion) = lektionDao.deleteLektion(lektion)

    // Vokabeln
    fun getVocabsByLektion(lektionId: Long): Flow<List<Vocab>> =
        vocabDao.getVocabsByLektion(lektionId)

    suspend fun insertVocab(vocab: Vocab) = vocabDao.insertVocab(vocab)

    suspend fun updateVocab(vocab: Vocab) = vocabDao.updateVocab(vocab)

    suspend fun deleteVocab(vocab: Vocab) = vocabDao.deleteVocab(vocab)
}

