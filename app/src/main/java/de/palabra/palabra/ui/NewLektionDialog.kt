package de.palabra.palabra.ui

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import de.palabra.palabra.R

class NewLektionDialog(
    context: Context,
    private val onLektionCreated: (titel: String, fromLang: String, toLang: String) -> Unit
) : Dialog(context) {

    private lateinit var titleInput: TextInputEditText
    private lateinit var fromLangInput: TextInputEditText
    private lateinit var toLangInput: TextInputEditText

    private lateinit var titleInputLayout: TextInputLayout
    private lateinit var fromLangInputLayout: TextInputLayout
    private lateinit var toLangInputLayout: TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_new_lekion, null)
        setContentView(view)

        titleInput = view.findViewById(R.id.titleInput)
        fromLangInput = view.findViewById(R.id.fromLangInput)
        toLangInput = view.findViewById(R.id.toLangInput)

        titleInputLayout = view.findViewById(R.id.titleInputLayout)
        fromLangInputLayout = view.findViewById(R.id.fromLangInputLayout)
        toLangInputLayout = view.findViewById(R.id.toLangInputLayout)

        val btnAdd = view.findViewById<Button>(R.id.btnAdd)
        val btnCancel = view.findViewById<Button>(R.id.btnCancel)

        btnAdd.setOnClickListener {
            clearErrors()
            if (validateFields()) {
                onLektionCreated(
                    titleInput.text.toString(),
                    fromLangInput.text.toString(),
                    toLangInput.text.toString()
                )
                dismiss()
            }
        }

        btnCancel.setOnClickListener {
            dismiss()
        }
    }

    private fun clearErrors() {
        titleInputLayout.error = null
        fromLangInputLayout.error = null
        toLangInputLayout.error = null
    }

    private fun validateFields(): Boolean {
        var isValid = true
        if (titleInput.text.isNullOrBlank()) {
            titleInputLayout.error = "Pflichtfeld"
            isValid = false
        }
        if (fromLangInput.text.isNullOrBlank()) {
            fromLangInputLayout.error = "Pflichtfeld"
            isValid = false
        }
        if (toLangInput.text.isNullOrBlank()) {
            toLangInputLayout.error = "Pflichtfeld"
            isValid = false
        }
        return isValid
    }
}