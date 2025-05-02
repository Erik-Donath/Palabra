package de.palabra.palabra.ui

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import de.palabra.palabra.R

class ResultFragment : Fragment() {

    private lateinit var correctWord: String
    private lateinit var selectedWord: String
    private var isCorrect: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            correctWord = it.getString("correctWord", "")!!
            selectedWord = it.getString("selectedWord", "")!!
            isCorrect = it.getBoolean("isCorrect")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val resultText = view.findViewById<TextView>(R.id.resultText)
        val wordText = view.findViewById<TextView>(R.id.wordText)
        val answerText = view.findViewById<TextView>(R.id.answerText)
        val nextBtn = view.findViewById<Button>(R.id.nextBtn)

        resultText.text = if (isCorrect) "Richtig!" else "Falsch!"
        resultText.setTextColor(if (isCorrect) Color.GREEN else Color.RED)
        wordText.text = correctWord
        answerText.text = selectedWord
        answerText.setTextColor(if (isCorrect) Color.GREEN else Color.RED)

        nextBtn.setOnClickListener {
            (activity as? LearnActivity)?.next()
        }
    }

    companion object {
        fun newInstance(correctWord: String, selectedWord: String, isCorrect: Boolean): ResultFragment {
            return ResultFragment().apply {
                arguments = bundleOf(
                    "correctWord" to correctWord,
                    "selectedWord" to selectedWord,
                    "isCorrect" to isCorrect
                )
            }
        }
    }
}
