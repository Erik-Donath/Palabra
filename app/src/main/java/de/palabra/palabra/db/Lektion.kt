package de.palabra.palabra.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "lektion")
data class Lektion(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0, // PK
    val name: String,
    val fromLang: String,
    val toLang: String
)

@Dao
interface LektionDao {
    @Query("SELECT id FROM lektion")
    suspend fun getLektionsIds(): List<Long>

    @Query("SELECT * FROM lektion")
    fun getAllLektions(): Flow<List<Lektion>>

    @Query("SELECT * FROM lektion WHERE id = :id")
    suspend fun getLektionById(id: Long): Lektion?

    @Insert
    suspend fun insertLektion(lektion: Lektion): Long

    @Update
    suspend fun updateLektion(lektion: Lektion)

    @Delete
    suspend fun deleteLektion(lektion: Lektion)
}
