package de.palabra.palabra.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface VocabDao {
    @Query("SELECT * FROM vocab WHERE lektionId = :lektionId")
    fun getVocabsForLektion(lektionId: Long): LiveData<List<Vocab>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVocab(vocab: Vocab): Long

    @Delete
    suspend fun deleteVocab(vocab: Vocab)
}