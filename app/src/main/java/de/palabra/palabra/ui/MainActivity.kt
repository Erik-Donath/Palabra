package de.palabra.palabra.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
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
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

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
}
