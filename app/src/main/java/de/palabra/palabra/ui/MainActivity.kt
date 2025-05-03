package de.palabra.palabra.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import de.palabra.palabra.R
import de.palabra.palabra.db.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Android Integration Stuff
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Create Database
        AppDatabase.create(this, CoroutineScope(SupervisorJob()))

        findViewById<Button>(R.id.settingsBtn).setOnClickListener {
            println("TODO: Settings")
        }

        findViewById<Button>(R.id.lektionBtn).setOnClickListener {
            println("Pressed Lektion Btn")
            startActivity(Intent(this, LearnActivity::class.java))
        }
        findViewById<Button>(R.id.smartBtn).setOnClickListener {
            println("Pressed Smart Btn")
            startActivity(Intent(this, LearnActivity::class.java))
        }
        findViewById<Button>(R.id.allBtn).setOnClickListener {
            println("Pressed All Btn")
            startActivity(Intent(this, LearnActivity::class.java))
        }
    }
}