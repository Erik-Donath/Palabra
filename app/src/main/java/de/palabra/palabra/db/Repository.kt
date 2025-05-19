package de.palabra.palabra.db

import kotlinx.coroutines.flow.Flow

class Repository(private val lektionDao: LektionDao) {
    fun getAllLektionenWithVocabs(): Flow<List<LektionWithVocabs>> = lektionDao.getAllLektionenWithVocabs()
    suspend fun getAllLektionenWithVocabsSuspend(): List<LektionWithVocabs>? = lektionDao.getAllLektionenWithVocabsSuspend()
    suspend fun getLektionWithVocabsSuspend(lektionId: Int): LektionWithVocabs? = lektionDao.getLektionWithVocabsSuspend(lektionId)
    suspend fun insertLektion(lektion: Lektion) = lektionDao.insertLektion(lektion)
    suspend fun insertVocab(vocab: Vocab) = lektionDao.insertVocab(vocab)
    suspend fun deleteLektion(lektion: Lektion) = lektionDao.deleteLektion(lektion)
    suspend fun deleteVocab(vocab: Vocab) = lektionDao.deleteVocab(vocab)
}