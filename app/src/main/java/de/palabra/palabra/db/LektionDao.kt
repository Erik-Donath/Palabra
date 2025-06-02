package de.palabra.palabra.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface LektionDao {
    @Transaction
    @Query("SELECT * FROM Lektion ORDER BY title")
    fun getAllLektionenWithVocabsFlow(): Flow<List<LektionWithVocabs>>

    @Transaction
    @Query("SELECT * FROM Lektion ORDER BY title")
    suspend fun getAllLektionenWithVocabs(): List<LektionWithVocabs>

    @Transaction
    @Query("SELECT * FROM Lektion WHERE id = :lektionId")
    suspend fun getLektionWithVocabs(lektionId: Int): LektionWithVocabs?

    @Query("SELECT * FROM Lektion ORDER BY title")
    suspend fun getAllLektionen(): List<Lektion>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLektion(lektion: Lektion): Long

    @Update
    suspend fun updateLektion(lektion: Lektion)

    @Delete
    suspend fun deleteLektion(lektion: Lektion)
}