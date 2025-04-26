package de.palabra.palabra

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class LearnActivity : AppCompatActivity(), GuessFragment.OnAnswerSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_learn)

        // Setup top navigation buttons (in activity, not fragments)
        val homeBtn = findViewById<ImageButton>(R.id.homeBtn)
        val listBtn = findViewById<ImageButton>(R.id.listBtn)

        homeBtn.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        listBtn.setOnClickListener {
            showVocabListFragment()
        }

        // Show the initial GuessFragment if this is first creation
        if (savedInstanceState == null) {
            showGuessFragment()
        }
    }

    private fun showGuessFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, GuessFragment())
            .commit()
    }

    private fun showVocabListFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, VocabListFragment())
            .commit()
    }

    // Callback from GuessFragment when user selects an answer
    override fun onAnswerSelected(selectedIndex: Int, isCorrect: Boolean) {
        val resultFragment = ResultFragment.newInstance(isCorrect, selectedIndex)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, resultFragment)
            .commit()
    }

    // Called from ResultFragment when user clicks "next"
    fun showNextWord() {
        showGuessFragment()
    }
}
