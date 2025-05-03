package de.palabra.palabra.ui

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels

class VocabListDialogFragment : DialogFragment() {
    private val viewModel: LearnViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val items = viewModel.sampleData.map { "${it.word} → ${it.solutions[it.correctIndex]}" }

        return AlertDialog.Builder(requireContext())
            .setTitle("Vokabelliste")
            .setItems(items.toTypedArray(), null)
            .setPositiveButton("Schließen", null)
            .create()
    }
}
