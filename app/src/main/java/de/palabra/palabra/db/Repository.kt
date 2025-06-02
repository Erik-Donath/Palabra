package de.palabra.palabra.db

import android.content.Context
import kotlinx.coroutines.flow.Flow

class Repository(context: Context) {
    private val db = PalabraDatabase.getInstance(context.applicationContext)
    private val lektionDao = db.lektionDao()
    private val vocabDao = db.vocabDao()

    fun getAllLektionenWithVocabsFlow(): Flow<List<LektionWithVocabs>> =
        lektionDao.getAllLektionenWithVocabsFlow()

    suspend fun getAllLektionenWithVocabs(): List<LektionWithVocabs> =
        lektionDao.getAllLektionenWithVocabs()

    suspend fun getLektionWithVocabs(lektionId: Int): LektionWithVocabs? =
        lektionDao.getLektionWithVocabs(lektionId)

    suspend fun getAllLektionen(): List<Lektion> = lektionDao.getAllLektionen()
    suspend fun insertLektion(lektion: Lektion): Long = lektionDao.insertLektion(lektion)
    suspend fun updateLektion(lektion: Lektion) = lektionDao.updateLektion(lektion)
    suspend fun deleteLektion(lektion: Lektion) = lektionDao.deleteLektion(lektion)

    suspend fun getVocabsForLektion(lektionId: Int): List<Vocab> =
        vocabDao.getVocabsForLektion(lektionId)
    suspend fun getAllVocabs(): List<Vocab> = vocabDao.getAllVocabs()
    suspend fun getVocabById(id: Int): Vocab? = vocabDao.getVocabById(id)
    suspend fun insertVocab(vocab: Vocab): Long = vocabDao.insertVocab(vocab)
    suspend fun updateVocab(vocab: Vocab) = vocabDao.updateVocab(vocab)
    suspend fun deleteVocab(vocab: Vocab) = vocabDao.deleteVocab(vocab)
    suspend fun deleteVocabsByLektion(lektionId: Int) = vocabDao.deleteVocabsByLektion(lektionId)
}