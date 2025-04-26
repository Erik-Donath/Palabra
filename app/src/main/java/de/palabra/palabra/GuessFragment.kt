package de.palabra.palabra

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment

class GuessFragment : Fragment() {

    private lateinit var listener: OnAnswerSelectedListener

    interface OnAnswerSelectedListener {
        fun onAnswerSelected(selectedIndex: Int, isCorrect: Boolean)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnAnswerSelectedListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnAnswerSelectedListener")
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

        // Example word data - in real app you would get from provider
        val wordText = view.findViewById<TextView>(R.id.wordText)
        val solution1Btn = view.findViewById<Button>(R.id.solution1Btn)
        val solution2Btn = view.findViewById<Button>(R.id.solution2Btn)
        val solution3Btn = view.findViewById<Button>(R.id.solution3Btn)

        // Set sample data
        wordText.text = "Hund"
        solution1Btn.text = "dog"
        solution2Btn.text = "cat"
        solution3Btn.text = "mouse"

        // Set click listeners for solutions
        solution1Btn.setOnClickListener {
            listener.onAnswerSelected(0, true) // 0 is correct in this example
        }

        solution2Btn.setOnClickListener {
            listener.onAnswerSelected(1, false)
        }

        solution3Btn.setOnClickListener {
            listener.onAnswerSelected(2, false)
        }
    }
}
