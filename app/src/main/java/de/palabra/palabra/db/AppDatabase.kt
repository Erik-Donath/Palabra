package de.palabra.palabra.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.CoroutineScope

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

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun create(context: Context, scope: CoroutineScope) {
            if (INSTANCE == null) {
                synchronized(this) {
                    if (INSTANCE == null) {
                        val instance = Room.databaseBuilder(
                            context.applicationContext,
                            AppDatabase::class.java,
                            "vocablulary"
                        )
                            .fallbackToDestructiveMigration()
                            .addCallback(AppDatabaseCallback(scope))
                            .build()

                        INSTANCE = instance
                    }
                }
            }
        }

        fun getDatabase(): AppDatabase {
            return INSTANCE ?: throw IllegalStateException(
                "AppDatabase muss zuerst mit create() initialisiert werden."
            )
        }

        private class AppDatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            // Callback-Implementierung wie gehabt
        }
    }
}
