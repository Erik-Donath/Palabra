package de.palabra.palabra.db

import kotlinx.coroutines.flow.Flow

class PalabraRepository(private val database: AppDatabase) {
    val allLektions: Flow<List<Lektion>> = database.lektionDao().getAllLektions()
    val allVocabs: Flow<List<Vocab>> = database.vocabDao().getAllVocabs()

    suspend fun insertLektion(lektion: Lektion): Long {
        return database.lektionDao().insertLektion(lektion)
    }

    suspend fun updateLektion(lektion: Lektion) {
        database.lektionDao().updateLektion(lektion)
    }

    suspend fun deleteLektion(lektion: Lektion) {
        database.lektionDao().deleteLektion(lektion)
    }

    fun getVocabsByLektion(lektionId: Long): Flow<List<Vocab>> {
        return database.vocabDao().getVocabsByLektion(lektionId)
    }

    suspend fun insertVocab(vocab: Vocab): Long {
        return database.vocabDao().insertVocab(vocab)
    }

    suspend fun updateVocab(vocab: Vocab) {
        database.vocabDao().updateVocab(vocab)
    }

    suspend fun deleteVocab(vocab: Vocab) {
        database.vocabDao().deleteVocab(vocab)
    }

    suspend fun getLektionsIds(): List<Long> {
        return database.getLektions()
    }

    suspend fun getVocabsIds(): List<Long> {
        return database.getVocabs()
    }
}
