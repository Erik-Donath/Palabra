package de.palabra.palabra.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import de.palabra.palabra.R
import de.palabra.palabra.databinding.FragmentResultBinding
import kotlinx.coroutines.launch

class ResultFragment : Fragment() {
    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!
    private val args: ResultFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val correct = args.pickedIndex == args.correctIndex
        binding.resultText.text = if (correct) getString(R.string.learn_correct) else getString(R.string.learn_wrong)
        binding.pickedOptionText.text = getString(R.string.learn_your_choice, args.solutions?.get(args.pickedIndex) ?: "")
        binding.correctOptionText.text = getString(R.string.learn_correct_solution, args.solutions?.get(args.correctIndex) ?: "")

        val provider = (requireActivity() as LearnActivity).provider
        lifecycleScope.launch {
            val vocab = provider.getVocabById(args.vocabId)
            vocab?.let {
                provider.submitResult(requireContext(), it.id, correct)
            }
        }

        binding.nextButton.setOnClickListener {
            findNavController().navigate(ResultFragmentDirections.actionResultToGuess())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}