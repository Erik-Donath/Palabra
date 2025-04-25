package de.palabra.palabra.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Lektion::class, Vocab::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun lektionDao(): LektionDao
    abstract fun vocabDao(): VocabDao

    suspend fun getLektions(): List<Long> {
        return lektionDao().getLektionsIds()
    }

    suspend fun getVocabs(): List<Long> {
        return vocabDao().getVocabsIds()
    }
}
