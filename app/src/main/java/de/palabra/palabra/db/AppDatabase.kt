package de.palabra.palabra.db

import android.content.Context
import androidx.databinding.adapters.Converters
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [Lektion::class, Vocab::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun lektionDao(): LektionDao
    abstract fun vocabDao(): VocabDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun initialize(context: Context) {
            synchronized(this) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "palabra.db"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            // Hier Testdaten einfügen wenn gewünscht
                        }
                    })
                    .build()
            }
        }

        fun getDatabase(): AppDatabase {
            return INSTANCE ?: throw IllegalStateException(
                "Database muss zuerst mit initialize() initialisiert werden"
            )
        }
    }
}
