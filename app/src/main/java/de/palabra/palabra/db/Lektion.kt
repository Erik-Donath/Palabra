package de.palabra.palabra.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Lektion(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val fromLangCode: String,
    val toLangCode: String,
    val description: String
)