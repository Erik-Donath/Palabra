package de.palabra.palabra.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import de.palabra.palabra.AllProviderFunction
import de.palabra.palabra.R
import de.palabra.palabra.SmartProviderFunction
import de.palabra.palabra.VocabProvider
import de.palabra.palabra.db.Repository
import de.palabra.palabra.util.applyEdgeToEdgeInsets
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {
    private lateinit var repository: Repository
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        applyEdgeToEdgeInsets<View>(R.id.main)

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
                    Toast.makeText(this@MainActivity, getString(R.string.no_vocab_to_learn), Toast.LENGTH_LONG).show()
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
                    Toast.makeText(this@MainActivity, getString(R.string.no_vocab_to_learn), Toast.LENGTH_LONG).show()
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
