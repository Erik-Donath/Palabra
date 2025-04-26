package de.palabra.palabra

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment

class ResultFragment : Fragment() {

    private var isCorrect: Boolean = false
    private var selectedIndex: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get arguments that were passed to this fragment
        arguments?.let {
            isCorrect = it.getBoolean(ARG_IS_CORRECT, false)
            selectedIndex = it.getInt(ARG_SELECTED_INDEX, 0)
        }

        // Set the result text and color
        val resultText = view.findViewById<TextView>(R.id.resultText)
        val wordText = view.findViewById<TextView>(R.id.wordText)
        val answerText = view.findViewById<TextView>(R.id.answerText)

        resultText.text = if (isCorrect) "Correct!" else "Wrong!"
        resultText.setTextColor(if (isCorrect) Color.GREEN else Color.RED)

        // Sample data - in real app, get from your provider
        wordText.text = "Hund"

        val answers = listOf("dog", "cat", "mouse")
        answerText.text = answers[selectedIndex]
        answerText.setTextColor(if (isCorrect) Color.GREEN else Color.RED)

        // Set up navigation buttons
        val backBtn = view.findViewById<Button>(R.id.backBtn)
        val nextBtn = view.findViewById<Button>(R.id.nextBtn)

        backBtn.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        nextBtn.setOnClickListener {
            (activity as? LearnActivity)?.showNextWord()
        }
    }

    companion object {
        private const val ARG_IS_CORRECT = "is_correct"
        private const val ARG_SELECTED_INDEX = "selected_index"

        fun newInstance(isCorrect: Boolean, selectedIndex: Int): ResultFragment {
            val fragment = ResultFragment()
            val args = Bundle()
            args.putBoolean(ARG_IS_CORRECT, isCorrect)
            args.putInt(ARG_SELECTED_INDEX, selectedIndex)
            fragment.arguments = args
            return fragment
        }
    }
}
