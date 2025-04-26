package de.palabra.palabra

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class LearnActivity : AppCompatActivity(), GuessFragment.OnAnswerSelectedListener {

    private val sampleData = listOf(
        GuessFragment.GuessData("Hund", listOf("dog", "cat", "mouse"), 0),
        GuessFragment.GuessData("Katze", listOf("bird", "cat", "horse"), 1),
        GuessFragment.GuessData("Haus", listOf("car", "tree", "house"), 2)
    )
    private var currentGuessData: GuessFragment.GuessData? = null
    private var currentIndex = 0

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
            next()
        }
    }

    fun next() {
        currentGuessData = sampleData[currentIndex]
        currentIndex = ++currentIndex % sampleData.size

        val guessFragment = GuessFragment.newInstance(currentGuessData!!)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, guessFragment)
            .commit()
    }

    override fun onAnswerSelected(selectedIndex: Int, isCorrect: Boolean) {
        val resultFragment = ResultFragment.newInstance(currentGuessData!!.word, currentGuessData!!.solutions[selectedIndex], isCorrect)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, resultFragment)
            .commit()
    }

    private fun showVocabListFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, VocabListFragment())
            .commit()
    }
}
