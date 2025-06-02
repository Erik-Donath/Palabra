package de.palabra.palabra.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import de.palabra.palabra.VocabProvider
import de.palabra.palabra.databinding.FragmentGuessBinding

class GuessFragment : Fragment() {
    private var _binding: FragmentGuessBinding? = null
    private val binding get() = _binding!!
    private lateinit var guess: VocabProvider.GuessData

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGuessBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val provider = (requireActivity() as LearnActivity).provider
        guess = provider.getNextGuess() ?: run {
            (activity as LearnActivity).finishSession()
            return
        }

        binding.wordText.text = guess.word
        val optionButtons = listOf(binding.option1, binding.option2, binding.option3)

        optionButtons.forEachIndexed { idx, button ->
            button.visibility = if (idx < guess.solutions.size) View.VISIBLE else View.GONE
            button.text = guess.solutions.getOrNull(idx) ?: ""
            button.setOnClickListener {
                val action = GuessFragmentDirections.actionGuessToResult(
                    guess.word,
                    guess.solutions.toTypedArray(),
                    guess.correctIndex,
                    idx // picked index
                )
                findNavController().navigate(action)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}