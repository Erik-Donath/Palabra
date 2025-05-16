package de.palabra.palabra.ui

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import de.palabra.palabra.R

class AddVocabDialogFragment(
    private val onAddVocab: (fromWord: String, toWord: String) -> Unit
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_add_vocab, null)
        val editFromWord = view.findViewById<EditText>(R.id.editFromWord)
        val editToWord = view.findViewById<EditText>(R.id.editToWord)
        val btnCancel = view.findViewById<Button>(R.id.btnCancel)
        val btnAdd = view.findViewById<Button>(R.id.btnAdd)

        btnCancel.setOnClickListener { dismiss() }
        btnAdd.setOnClickListener {
            val fromWord = editFromWord.text.toString().trim()
            val toWord = editToWord.text.toString().trim()
            if (fromWord.isBlank()) {
                editFromWord.error = "Required"
                return@setOnClickListener
            }
            if (toWord.isBlank()) {
                editToWord.error = "Required"
                return@setOnClickListener
            }
            onAddVocab(fromWord, toWord)
            dismiss()
        }

        return AlertDialog.Builder(requireContext())
            .setView(view)
            .create()
    }
}