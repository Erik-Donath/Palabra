package de.palabra.palabra.db

import androidx.room.Embedded
import androidx.room.Relation

data class LektionWithVocabs(
    @Embedded val lektion: Lektion,
    @Relation(
        parentColumn = "id",
        entityColumn = "lektionId"
    )
    val vocabs: List<Vocab>
)