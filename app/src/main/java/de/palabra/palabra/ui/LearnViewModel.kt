package de.palabra.palabra.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import de.palabra.palabra.GuessData

class LearnViewModel : ViewModel() {
    val sampleData = listOf(
        GuessData("Hund", listOf("dog", "cat", "mouse"), 0),
        GuessData("Katze", listOf("bird", "cat", "horse"), 1),
        GuessData("Haus", listOf("car", "tree", "house"), 2),
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
        if (currentIndex >= sampleData.size) {
            _navigationEvent.value = NavigationEvent.Finish // oder ShowEnd
        } else {
            _currentGuess.value = sampleData[currentIndex]
            currentIndex++
        }
    }


    fun handleAnswer(selectedIndex: Int, isCorrect: Boolean) {
        val currentData = sampleData.getOrNull(currentIndex - 1) ?: run {
            _navigationEvent.value = NavigationEvent.Finish
            return
        }

        _navigationEvent.value = NavigationEvent.ShowResult(
            correctWord = currentData.word,
            selectedWord = currentData.solutions.getOrNull(selectedIndex) ?: "",
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
        object Finish : NavigationEvent()
    }
}
