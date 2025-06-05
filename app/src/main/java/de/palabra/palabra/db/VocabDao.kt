package de.palabra.palabra.db

import androidx.room.*

@Dao
interface VocabDao {
    @Query("SELECT * FROM Vocab WHERE lektionId = :lektionId")
    suspend fun getVocabsForLektion(lektionId: Int): List<Vocab>

    @Query("SELECT * FROM Vocab")
    suspend fun getAllVocabs(): List<Vocab>

    @Query("SELECT * FROM Vocab WHERE id = :id")
    suspend fun getVocabById(id: Int): Vocab?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVocab(vocab: Vocab): Long

    @Update
    suspend fun updateVocab(vocab: Vocab)

    @Delete
    suspend fun deleteVocab(vocab: Vocab)

    @Query("DELETE FROM Vocab WHERE lektionId = :lektionId")
    suspend fun deleteVocabsByLektion(lektionId: Int)

    @Query("UPDATE Vocab SET correctCount = correctCount + 1 WHERE id = :vocabId")
    suspend fun incrementCorrectCount(vocabId: Int)

    @Query("UPDATE Vocab SET wrongCount = wrongCount + 1 WHERE id = :vocabId")
    suspend fun incrementWrongCount(vocabId: Int)

    @Query("SELECT SUM(correctCount) FROM Vocab")
    suspend fun getTotalCorrectCount(): Int?

    @Query("SELECT SUM(wrongCount) FROM Vocab")
    suspend fun getTotalWrongCount(): Int?
}
