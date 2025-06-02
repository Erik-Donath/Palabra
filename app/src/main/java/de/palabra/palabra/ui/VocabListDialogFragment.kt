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
        val vocabsWithStatus = provider.getList()
        val items = vocabsWithStatus.map { (vocab, answer, _) ->
            val status = when (answer) {
                true -> "✔"
                false -> "✘"
                null -> ""
            }
            "${vocab.word} - ${vocab.toWord} $status"
        }
        val statuses = vocabsWithStatus.map { it.second }
        binding.vocabListView.adapter = VocabListAdapter(requireContext(), items, statuses)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .create()
            .apply {
                window?.setBackgroundDrawableResource(de.palabra.palabra.R.drawable.bg_rounded)
            }

        binding.dialogCloseBtn.setOnClickListener {
            dialog.dismiss()
        }
        return dialog
    }
}