package de.palabra.palabra.db

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = Lektion::class,
        parentColumns = ["id"],
        childColumns = ["lektionId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("lektionId")]
)
data class Vocab(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val word: String,
    val toWord: String,
    val lektionId: Int
)