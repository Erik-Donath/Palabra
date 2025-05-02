package de.palabra.palabra.ui

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import de.palabra.palabra.GuessData
import de.palabra.palabra.R

class GuessFragment : Fragment() {

    interface OnAnswerSelectedListener {
        fun onAnswerSelected(selectedIndex: Int, isCorrect: Boolean)
    }

    private lateinit var listener: OnAnswerSelectedListener
    private lateinit var guessData: GuessData

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnAnswerSelectedListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnAnswerSelectedListener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            guessData = it.getParcelable("data", GuessData::class.java)!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_guess, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val wordText = view.findViewById<TextView>(R.id.wordText)
        val solution1Btn = view.findViewById<Button>(R.id.solution1Btn)
        val solution2Btn = view.findViewById<Button>(R.id.solution2Btn)
        val solution3Btn = view.findViewById<Button>(R.id.solution3Btn)

        wordText.text = guessData.word
        solution1Btn.text = guessData.solutions[0]
        solution2Btn.text = guessData.solutions[1]
        solution3Btn.text = guessData.solutions[2]

        solution1Btn.setOnClickListener {
            listener.onAnswerSelected(0, guessData.correctIndex == 0)
        }
        solution2Btn.setOnClickListener {
            listener.onAnswerSelected(1, guessData.correctIndex == 1)
        }
        solution3Btn.setOnClickListener {
            listener.onAnswerSelected(2, guessData.correctIndex == 2)
        }
    }

    companion object {
        fun newInstance(guessData: GuessData): GuessFragment {
            return GuessFragment().apply {
                arguments = bundleOf("data" to guessData)
            }
        }
    }
}
