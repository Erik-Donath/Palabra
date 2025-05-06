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
        AppDatabase.initialize(applicationContext)

        findViewById<Button>(R.id.settingsBtn).setOnClickListener {
            startActivity(Intent(this, LicensActivity::class.java))
        }
        findViewById<Button>(R.id.lektionBtn).setOnClickListener {
            startActivity(Intent(this, LektionActivity::class.java))
        }
        findViewById<Button>(R.id.smartBtn).setOnClickListener {
            startActivity(Intent(this, LearnActivity::class.java))
        }
        findViewById<Button>(R.id.allBtn).setOnClickListener {
            startActivity(Intent(this, LearnActivity::class.java))
        }
    }
}