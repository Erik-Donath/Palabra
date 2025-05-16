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
                val lektionId1 = repository.insertLektion(
                    Lektion(
                        title = "Test Lektion",
                        fromLangCode = "de",
                        toLangCode = "us",
                        description = "Dies ist eine Testlektion"
                    )
                ).toInt()

                val vocabs1 = listOf(
                    Vocab(word = "Hund", toWord = "dog", lektionId = lektionId1),
                    Vocab(word = "Katze", toWord = "cat", lektionId = lektionId1),
                    Vocab(word = "Haus", toWord = "house", lektionId = lektionId1),
                    Vocab(word = "Baum", toWord = "tree", lektionId = lektionId1),
                    Vocab(word = "Buch", toWord = "book", lektionId = lektionId1)
                )
                vocabs1.forEach { repository.insertVocab(it) }

                val lektionId2 = repository.insertLektion(
                    Lektion(
                        title = "Woyzeck",
                        fromLangCode = "de",
                        toLangCode = "eu",
                        description = "Aber wenn mir die Natur kommt?"
                    )
                ).toInt()

                val vocabs2 = listOf(
                    Vocab(word = "Marie", toWord = "Marie", lektionId = lektionId2),
                    Vocab(word = "Woyzeck", toWord = "Woyzeck", lektionId = lektionId2),
                    Vocab(word = "Hauptman", toWord = "Hauptman", lektionId = lektionId2),
                    Vocab(word = "Tambormajor", toWord = "Tambormajor", lektionId = lektionId2),
                    Vocab(word = "Doktor", toWord = "Artzt", lektionId = lektionId2)
                )
                vocabs2.forEach { repository.insertVocab(it) }
            }
        }
    }
}