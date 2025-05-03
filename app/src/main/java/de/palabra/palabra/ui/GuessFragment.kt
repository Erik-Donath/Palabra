package de.palabra.palabra.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import de.palabra.palabra.GuessData
import de.palabra.palabra.databinding.FragmentGuessBinding

class GuessFragment : Fragment() {
    private var _binding: FragmentGuessBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LearnViewModel by activityViewModels()
    private lateinit var guessData: GuessData

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
    ): View {
        _binding = FragmentGuessBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.wordText.text = guessData.word
        binding.solution1Btn.text = guessData.solutions[0]
        binding.solution2Btn.text = guessData.solutions[1]
        binding.solution3Btn.text = guessData.solutions[2]

        binding.solution1Btn.setOnClickListener { viewModel.handleAnswer(0, guessData.correctIndex == 0) }
        binding.solution2Btn.setOnClickListener { viewModel.handleAnswer(1, guessData.correctIndex == 1) }
        binding.solution3Btn.setOnClickListener { viewModel.handleAnswer(2, guessData.correctIndex == 2) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(guessData: GuessData) = GuessFragment().apply {
            arguments = Bundle().apply {
                putParcelable("data", guessData)
            }
        }
    }
}
