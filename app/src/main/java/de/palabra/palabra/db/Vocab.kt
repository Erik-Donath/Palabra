package de.palabra.palabra.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Entity(
    tableName = "vocab",
    foreignKeys = [
        ForeignKey(
            entity = Lektion::class,
            parentColumns = ["id"],
            childColumns = ["lektionId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["lektionId"])]
)
data class Vocab(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val lektionId: Long,
    val fromWord: String,
    val toWord: String
)


@Dao
interface VocabDao {
    @Query("SELECT * FROM vocab WHERE lektionId = :lektionId ORDER BY fromWord ASC")
    fun getVocabsByLektion(lektionId: Long): Flow<List<Vocab>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVocab(vocab: Vocab): Long

    @Update
    suspend fun updateVocab(vocab: Vocab)

    @Delete
    suspend fun deleteVocab(vocab: Vocab)
}

