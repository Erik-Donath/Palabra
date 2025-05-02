package de.palabra.palabra.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import de.palabra.palabra.GuessData

class LearnViewModel : ViewModel() {
    val sampleData = listOf(
        GuessData("Hund", listOf("dog", "cat", "mouse"), 0),
        GuessData("Katze", listOf("bird", "cat", "horse"), 1),
        GuessData("Haus", listOf("car", "tree", "house"), 2)
    )

    private val _currentGuess = MutableLiveData<GuessData>()
    val currentGuess: LiveData<GuessData> = _currentGuess

    private val _navigationEvent = MutableLiveData<NavigationEvent>()
    val navigationEvent: LiveData<NavigationEvent> = _navigationEvent

    private var currentIndex = 0

    init {
        loadNext()
    }

    fun loadNext() {
        _currentGuess.value = sampleData[currentIndex]
        currentIndex = (currentIndex + 1) % sampleData.size
    }

    fun handleAnswer(selectedIndex: Int, isCorrect: Boolean) {
        _navigationEvent.value = NavigationEvent.ShowResult(
            correctWord = sampleData[currentIndex - 1].word,
            selectedWord = sampleData[currentIndex - 1].solutions[selectedIndex],
            isCorrect = isCorrect
        )
    }

    fun triggerNext() {
        _navigationEvent.value = NavigationEvent.ShowNextQuestion
    }

    sealed class NavigationEvent {
        data class ShowResult(
            val correctWord: String,
            val selectedWord: String,
            val isCorrect: Boolean
        ) : NavigationEvent()

        object ShowNextQuestion : NavigationEvent()
    }
}
