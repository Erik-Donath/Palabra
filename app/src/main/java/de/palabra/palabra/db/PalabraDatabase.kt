package de.palabra.palabra.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Lektion::class, Vocab::class],
    version = 1,
    exportSchema = true
)
abstract class PalabraDatabase : RoomDatabase() {
    abstract fun lektionDao(): LektionDao
    abstract fun vocabDao(): VocabDao

    companion object {
        @Volatile
        private var INSTANCE: PalabraDatabase? = null

        fun getInstance(context: Context): PalabraDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context.applicationContext).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext, PalabraDatabase::class.java, "palabra_database")
                .fallbackToDestructiveMigration(true)
                .build()
    }
}