package de.palabra.palabra.ui

import android.app.Dialog
import android.content.Context
import android.widget.Button
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import de.palabra.palabra.R


class NewVocabDialog(
    context: Context,
    private val lektionId: Long,
    private val onVocabCreated: (Pair<String, String>) -> Unit
) : Dialog(context) {

    init {
        setContentView(R.layout.dialog_new_vocab)
        setupUi()
    }

    private fun setupUi() {
        val btnAdd = findViewById<Button>(R.id.btnAdd)
        val btnCancel = findViewById<Button>(R.id.btnCancel)
        val fromInput = findViewById<TextInputEditText>(R.id.fromWordInput)
        val toInput = findViewById<TextInputEditText>(R.id.toWordInput)

        btnCancel.setOnClickListener { dismiss() }
        btnAdd.setOnClickListener {
            val fromWord = fromInput.text?.toString()?.trim() ?: ""
            val toWord = toInput.text?.toString()?.trim() ?: ""
            if (validateInput(fromWord, toWord)) {
                onVocabCreated(Pair(fromWord, toWord))
                dismiss()
            }
        }
    }

    private fun validateInput(from: String, to: String): Boolean {
        var valid = true
        val fromLayout = findViewById<TextInputLayout>(R.id.fromWordInputLayout)
        val toLayout = findViewById<TextInputLayout>(R.id.toWordInputLayout)
        if (from.isEmpty()) {
            fromLayout.error = "Eingabe erforderlich"
            valid = false
        } else fromLayout.error = null
        if (to.isEmpty()) {
            toLayout.error = "Eingabe erforderlich"
            valid = false
        } else toLayout.error = null
        return valid
    }
}
