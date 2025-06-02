package de.palabra.palabra

import android.app.Application
import de.palabra.palabra.db.Repository

class PalabraApplication : Application() {
    lateinit var repository: Repository
        private set

    override fun onCreate() {
        super.onCreate()
        initialize()
    }

    private fun initialize() {
        repository = Repository(this)
    }
}