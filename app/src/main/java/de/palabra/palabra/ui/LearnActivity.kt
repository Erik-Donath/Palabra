package de.palabra.palabra.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import de.palabra.palabra.GuessData
import de.palabra.palabra.R

class LearnActivity : AppCompatActivity(), GuessFragment.OnAnswerSelectedListener {

    private val sampleData = listOf(
        GuessData("Hund", listOf("dog", "cat", "mouse"), 0),
        GuessData("Katze", listOf("bird", "cat", "horse"), 1),
        GuessData("Haus", listOf("car", "tree", "house"), 2)
    )

    private var currentGuessData: GuessData? = null
    private var currentIndex = 0
    private var isVocabListVisible: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_learn)

        // Setup navigation buttons
        val homeBtn = findViewById<ImageButton>(R.id.homeBtn)
        val listBtn = findViewById<ImageButton>(R.id.listBtn)
        val vocabListOverlay = findViewById<LinearLayout>(R.id.vocabListOverlay)
        val vocabListView = findViewById<ListView>(R.id.vocabListView)

        homeBtn.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        isVocabListVisible = false
        vocabListOverlay.visibility = View.GONE

        listBtn.setOnClickListener {
            isVocabListVisible = !isVocabListVisible
            vocabListOverlay.visibility = if (isVocabListVisible) View.VISIBLE else View.GONE
        }

        val vocabList = sampleData.map { "${it.word} -> ${it.solutions[it.correctIndex]}" }
        vocabListView.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            vocabList
        )

        // Show the initial GuessFragment
        if (savedInstanceState == null) {
            next()
        }
    }

    fun next() {
        currentGuessData = sampleData[currentIndex]
        currentIndex = (currentIndex + 1) % sampleData.size

        val guessFragment = GuessFragment.newInstance(currentGuessData!!)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, guessFragment)
            .commit()
    }

    override fun onAnswerSelected(selectedIndex: Int, isCorrect: Boolean) {
        val resultFragment = ResultFragment.newInstance(
            currentGuessData!!.word,
            currentGuessData!!.solutions[selectedIndex],
            isCorrect
        )
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, resultFragment)
            .commit()
    }
}
