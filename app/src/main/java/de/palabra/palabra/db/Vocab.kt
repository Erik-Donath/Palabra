package de.palabra.palabra.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Entity(
    tableName = "vocab",
    foreignKeys = [
        ForeignKey(
            entity = Lektion::class,
            parentColumns = ["id"],
            childColumns = ["lektion"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Vocab(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0, // PK
    val lektion: Long, // FK
    val fromWord: String,
    val toWord: String
)

@Dao
interface VocabDao {
    @Query("SELECT id FROM vocab")
    suspend fun getVocabsIds(): List<Long>

    @Query("SELECT * FROM vocab WHERE lektion = :lektionId")
    fun getVocabsByLektion(lektionId: Long): Flow<List<Vocab>>

    @Query("SELECT * FROM vocab")
    fun getAllVocabs(): Flow<List<Vocab>>

    @Query("SELECT * FROM vocab WHERE id = :id")
    suspend fun getVocabById(id: Long): Vocab?

    @Insert
    suspend fun insertVocab(vocab: Vocab): Long

    @Update
    suspend fun updateVocab(vocab: Vocab)

    @Delete
    suspend fun deleteVocab(vocab: Vocab)
}
