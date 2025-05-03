package de.palabra.palabra.ui

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import de.palabra.palabra.databinding.FragmentResultBinding

class ResultFragment : Fragment() {
    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LearnViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            binding.resultText.text = if (it.getBoolean("isCorrect")) "Richtig!" else "Falsch!"
            binding.resultText.setTextColor(if (it.getBoolean("isCorrect")) Color.GREEN else Color.RED)
            binding.wordText.text = it.getString("correctWord")
            binding.answerText.text = it.getString("selectedWord")
        }

        binding.nextBtn.setOnClickListener { viewModel.triggerNext() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(correctWord: String, selectedWord: String, isCorrect: Boolean) =
            ResultFragment().apply {
                arguments = Bundle().apply {
                    putString("correctWord", correctWord)
                    putString("selectedWord", selectedWord)
                    putBoolean("isCorrect", isCorrect)
                }
            }
    }
}
