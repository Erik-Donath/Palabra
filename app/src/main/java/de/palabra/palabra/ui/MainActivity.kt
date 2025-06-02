package de.palabra.palabra.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import de.palabra.palabra.AllProvider
import de.palabra.palabra.PalabraApplication
import de.palabra.palabra.R
import de.palabra.palabra.SmartProvider
import de.palabra.palabra.db.LektionWithVocabs
import kotlinx.coroutines.launch

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

        findViewById<Button>(R.id.settingsBtn).setOnClickListener {
            startActivity(Intent(this, LicensActivity::class.java))
        }
        findViewById<Button>(R.id.lektionBtn).setOnClickListener {
            startActivity(Intent(this, LektionActivity::class.java))
        }
        findViewById<Button>(R.id.smartBtn).setOnClickListener {
            val provider = SmartProvider()
            val intent = Intent(this, LearnActivity::class.java)
            intent.putExtra(LearnActivity.EXTRA_PROVIDER, provider)
            startActivity(intent)
        }
        findViewById<Button>(R.id.allBtn).setOnClickListener {
            lifecycleScope.launch {
                val provider = AllProvider.create(this@MainActivity)
                if (provider != null && provider.getGuessList().isNotEmpty()) {
                    val intent = Intent(this@MainActivity, LearnActivity::class.java)
                    intent.putExtra(LearnActivity.EXTRA_PROVIDER, provider)
                    startActivity(intent)
                } else {
                    // Handle empty state
                    Log.w("Main", "There are no vocab's registered to learn.")
                }
            }
        }
    }
}