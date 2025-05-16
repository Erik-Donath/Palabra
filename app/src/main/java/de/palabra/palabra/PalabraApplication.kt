package de.palabra.palabra

import android.app.Application
import androidx.room.Room
import de.palabra.palabra.db.AppDatabase
import de.palabra.palabra.db.Repository

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
    }
}