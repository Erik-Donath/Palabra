package de.palabra.palabra

import android.app.Application
import androidx.room.Room
import de.palabra.palabra.db.AppDatabase
import de.palabra.palabra.db.Lektion
import de.palabra.palabra.db.Repository
import de.palabra.palabra.db.Vocab
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class PalabraApplication : Application() {
    lateinit var repository: Repository
        private set

    override fun onCreate() {
        super.onCreate()
        val db = Room.databaseBuilder(
            this,
            AppDatabase::class.java,
            "palabra.db"
        ).build()
        repository = Repository(db.lektionDao())

        CoroutineScope(Dispatchers.IO).launch {
            val lektions = repository.getAllLektionenWithVocabs().first()
            if (lektions.isEmpty()) {
                val lektionId = repository.insertLektion(
                    Lektion(
                        title = "Test Lektion",
                        fromLangCode = "de",
                        toLangCode = "us",
                        description = "Dies ist eine Testlektion"
                    )
                ).toInt()

                val vocabs = listOf(
                    Vocab(word = "Hund", toWord = "dog", lektionId = lektionId),
                    Vocab(word = "Katze", toWord = "cat", lektionId = lektionId),
                    Vocab(word = "Haus", toWord = "house", lektionId = lektionId),
                    Vocab(word = "Baum", toWord = "tree", lektionId = lektionId),
                    Vocab(word = "Buch", toWord = "book", lektionId = lektionId)
                )
                vocabs.forEach { repository.insertVocab(it) }
            }
        }
    }
}