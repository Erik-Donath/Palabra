package de.palabra.palabra

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
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

        AppDatabase.create(this, CoroutineScope(SupervisorJob()))

        val lektionBtn = findViewById<Button>(R.id.lektionBtn)
        val smartBtn = findViewById<Button>(R.id.smartBtn)
        val allBtn = findViewById<Button>(R.id.allBtn)

        lektionBtn.setOnClickListener {
            println("Pressed Lektion Btn")
            startActivity(Intent(this, LearnActivity::class.java))
            finish()
        }
        smartBtn.setOnClickListener {
            println("Pressed Smart Btn")
            startActivity(Intent(this, LearnActivity::class.java))
            finish()
        }
        allBtn.setOnClickListener {
            println("Pressed All Btn")
            startActivity(Intent(this, LearnActivity::class.java))
            finish()
        }
    }
}