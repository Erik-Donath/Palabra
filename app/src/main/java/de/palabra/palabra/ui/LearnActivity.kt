package de.palabra.palabra.ui

import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import de.palabra.palabra.GuessData
import de.palabra.palabra.R

class LearnActivity : AppCompatActivity() {
    private val viewModel: LearnViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_learn)

        findViewById<ImageButton>(R.id.homeBtn).setOnClickListener { finish() }
        findViewById<ImageButton>(R.id.listBtn).setOnClickListener {
            VocabListDialogFragment().show(supportFragmentManager, "vocabList")
        }

        viewModel.currentGuess.observe(this) { guess ->
            showGuessFragment(guess)
        }

        viewModel.navigationEvent.observe(this) { event ->
            if (isFinishing || isDestroyed) return@observe
            when (event) {
                is LearnViewModel.NavigationEvent.ShowResult -> showResultFragment(event)
                is LearnViewModel.NavigationEvent.ShowNextQuestion -> viewModel.loadNext()
                is LearnViewModel.NavigationEvent.Finish -> finish()
            }
        }
    }

    private fun showGuessFragment(guess: GuessData) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, GuessFragment.newInstance(guess))
            //.addToBackStack(null)
            .commit()
    }

    private fun showResultFragment(event: LearnViewModel.NavigationEvent.ShowResult) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, ResultFragment.newInstance(
                event.correctWord,
                event.selectedWord,
                event.isCorrect
            ))
            //.addToBackStack("result")
            .commit()
    }
}
