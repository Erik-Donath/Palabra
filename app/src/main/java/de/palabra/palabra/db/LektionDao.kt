package de.palabra.palabra.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface LektionDao {
    @Transaction
    @Query("SELECT * FROM Lektion ORDER BY title")
    fun getAllLektionenWithVocabs(): Flow<List<LektionWithVocabs>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLektion(lektion: Lektion): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVocab(vocab: Vocab): Long

    @Delete
    suspend fun deleteLektion(lektion: Lektion)

    @Delete
    suspend fun deleteVocab(vocab: Vocab)
}