package de.palabra.palabra.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Entity(
    tableName = "lektion",
    indices = [Index(value = ["name"], unique = true)]
)
data class Lektion(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val fromLang: String,
    val toLang: String
)


@Dao
interface LektionDao {
    @Query("SELECT * FROM lektion ORDER BY name ASC")
    fun getAllLektions(): Flow<List<Lektion>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLektion(lektion: Lektion): Long

    @Update
    suspend fun updateLektion(lektion: Lektion)

    @Delete
    suspend fun deleteLektion(lektion: Lektion)
}

