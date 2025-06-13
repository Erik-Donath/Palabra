package de.palabra.palabra.ui

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import de.palabra.palabra.R
import de.palabra.palabra.VocabProvider
import de.palabra.palabra.util.applyEdgeToEdgeInsets
import de.palabra.palabra.util.getSerializableCompat

class LearnActivity : AppCompatActivity() {
    lateinit var provider: VocabProvider
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_learn)
        applyEdgeToEdgeInsets<View>(R.id.learn)

        findViewById<ImageButton>(R.id.homeBtn).setOnClickListener {
            finish()
        }
        findViewById<ImageButton>(R.id.listBtn).setOnClickListener {
            VocabListDialogFragment().show(supportFragmentManager, "vocabListDialog")
        }

        provider = intent.getSerializableCompat(EXTRA_PROVIDER)
            ?: throw IllegalArgumentException("Provider must be passed to LearnActivity!")
    }

    fun finishSession() {
        finish()
    }

    companion object {
        const val EXTRA_PROVIDER = "extra_provider"
    }
}