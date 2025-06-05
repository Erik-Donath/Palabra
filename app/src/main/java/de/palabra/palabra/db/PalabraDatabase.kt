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
                // 1. Add columns
                database.execSQL("ALTER TABLE Vocab ADD COLUMN correctCount INTEGER NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE Vocab ADD COLUMN wrongCount INTEGER NOT NULL DEFAULT 0")

                // 2. Recreate table with NOT NULL and ON UPDATE CASCADE
                database.execSQL("""
                    CREATE TABLE Vocab_new (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        word TEXT NOT NULL,
                        toWord TEXT NOT NULL,
                        lektionId INTEGER NOT NULL,
                        correctCount INTEGER NOT NULL,
                        wrongCount INTEGER NOT NULL,
                        FOREIGN KEY(lektionId) REFERENCES Lektion(id) ON DELETE CASCADE ON UPDATE CASCADE
                    )
                """.trimIndent())

                // 3. Migrate data
                database.execSQL("""
                    INSERT INTO Vocab_new (id, word, toWord, lektionId, correctCount, wrongCount)
                    SELECT id, word, toWord, lektionId, correctCount, wrongCount FROM Vocab
                """.trimIndent())

                // 4. Replace old table
                database.execSQL("DROP TABLE Vocab")
                database.execSQL("ALTER TABLE Vocab_new RENAME TO Vocab")
                database.execSQL("CREATE INDEX index_Vocab_lektionId ON Vocab(lektionId)")
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
                "palabra.db"
            )
                .addMigrations(MIGRATION_1_2)
                .fallbackToDestructiveMigration(true)
                .build()
    }
}
