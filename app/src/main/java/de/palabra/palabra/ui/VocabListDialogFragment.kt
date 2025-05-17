package de.palabra.palabra.ui

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import de.palabra.palabra.databinding.DialogVocabListBinding

class VocabListDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = DialogVocabListBinding.inflate(LayoutInflater.from(requireContext()))
        val provider = (requireActivity() as LearnActivity).provider
        val items = provider.getGuessList().map { "${it.word} - ${it.solutions[it.correctIndex]}" }
        binding.vocabListView.adapter = VocabListAdapter(requireContext(), items)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .create()
            .apply {
                window?.setBackgroundDrawableResource(de.palabra.palabra.R.drawable.rounded_bg)
            }

        binding.dialogCloseBtn.setOnClickListener {
            dialog.dismiss()
        }
        return dialog
    }
}