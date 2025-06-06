package de.palabra.palabra.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import de.palabra.palabra.AllProviderFunction
import de.palabra.palabra.R
import de.palabra.palabra.SmartProviderFunction
import de.palabra.palabra.VocabProvider
import de.palabra.palabra.db.Repository
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {
    private lateinit var repository: Repository
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        repository = Repository(this)
        updateScore()

        findViewById<Button>(R.id.settingsBtn).setOnClickListener {
            startActivity(Intent(this, LicenseActivity::class.java))
        }
        findViewById<Button>(R.id.lektionBtn).setOnClickListener {
            startActivity(Intent(this, LektionActivity::class.java))
        }
        findViewById<Button>(R.id.smartBtn).setOnClickListener {
            lifecycleScope.launch {
                val provider = VocabProvider(SmartProviderFunction(this@MainActivity))

                if (provider.isNotEmpty()) {
                    val intent = Intent(this@MainActivity, LearnActivity::class.java)
                    intent.putExtra(LearnActivity.EXTRA_PROVIDER, provider)
                    startActivity(intent)
                } else {
                    Toast.makeText(this@MainActivity, "Keine Vokabeln um zu lernen", Toast.LENGTH_LONG).show()
                    Log.w("Main", "There are no vocab's registered to learn.")
                }
            }
        }
        findViewById<Button>(R.id.allBtn).setOnClickListener {
            lifecycleScope.launch {
                val provider = VocabProvider(AllProviderFunction(this@MainActivity))

                if (provider.isNotEmpty()) {
                    val intent = Intent(this@MainActivity, LearnActivity::class.java)
                    intent.putExtra(LearnActivity.EXTRA_PROVIDER, provider)
                    startActivity(intent)
                } else {
                    Toast.makeText(this@MainActivity, "Keine Vokabeln um zu lernen", Toast.LENGTH_LONG).show()
                    Log.w("Main", "There are no vocab's registered to learn.")
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        updateScore()
    }

    private fun updateScore() {
        lifecycleScope.launch {
            try {
                val correctCount = repository.getTotalCorrectCount()
                val wrongCount = repository.getTotalWrongCount()
                val totalAnswers = correctCount + wrongCount
                
                val scoreText = if (totalAnswers == 0) {
                    "Score: 0% (0/0)"
                } else {
                    val percentage = (correctCount.toDouble() / totalAnswers * 100).roundToInt()
                    "Score: $percentage% ($correctCount/$totalAnswers)"
                }
                
                findViewById<TextView>(R.id.score).text = scoreText
            } catch (e: Exception) {
                Log.e("MainActivity", "Error updating score", e)
                findViewById<TextView>(R.id.score).text = "Score: --"
            }
        }
    }
}
