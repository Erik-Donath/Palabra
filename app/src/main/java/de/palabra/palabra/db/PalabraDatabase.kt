package de.palabra.palabra.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [Lektion::class, Vocab::class],
    version = 2,
    exportSchema = true
)
abstract class PalabraDatabase : RoomDatabase() {
    abstract fun lektionDao(): LektionDao
    abstract fun vocabDao(): VocabDao

    companion object {
        @Volatile
        private var INSTANCE: PalabraDatabase? = null

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE Vocab ADD COLUMN correctCount INTEGER NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE Vocab ADD COLUMN wrongCount INTEGER NOT NULL DEFAULT 0")
            }
        }

        fun getInstance(context: Context): PalabraDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context.applicationContext).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                PalabraDatabase::class.java,
                "palabra_database"
            )
                .addMigrations(MIGRATION_1_2)
                .fallbackToDestructiveMigration(true)
                .build()
    }
}