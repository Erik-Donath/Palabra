package de.palabra.palabra.ui

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import de.palabra.palabra.R
import de.palabra.palabra.util.LanguageUtil

class AddLektionDialogFragment(
    private val onAddLektion: (title: String, fromLang: String, toLang: String, description: String) -> Unit
) : DialogFragment() {

    private lateinit var langCodes: List<String>
    private lateinit var langNames: List<String>

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.dialog_add_lektion, null)

        val editTitle = view.findViewById<EditText>(R.id.editTitle)
        val spinnerFromLang = view.findViewById<Spinner>(R.id.spinnerFromLang)
        val spinnerToLang = view.findViewById<Spinner>(R.id.spinnerToLang)
        val flagToLang = view.findViewById<ImageView>(R.id.flagToLang)
        val editDescription = view.findViewById<EditText>(R.id.editDescription)
        val btnCancel = view.findViewById<Button>(R.id.btnCancel)
        val btnAdd = view.findViewById<Button>(R.id.btnAdd)

        langCodes = LanguageUtil.languageCodes
        langNames = LanguageUtil.languageNames

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, langNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerFromLang.adapter = adapter
        spinnerToLang.adapter = adapter

        spinnerToLang.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
                val code = langCodes[pos]
                flagToLang.setImageResource(LanguageUtil.getFlagFromCode(code))
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
        // Set initial flag
        flagToLang.setImageResource(LanguageUtil.getFlagFromCode(langCodes[spinnerToLang.selectedItemPosition]))

        btnCancel.setOnClickListener { dismiss() }
        btnAdd.setOnClickListener {
            val title = editTitle.text.toString().trim()
            val fromLang = langCodes[spinnerFromLang.selectedItemPosition]
            val toLang = langCodes[spinnerToLang.selectedItemPosition]
            val desc = editDescription.text.toString().trim()
            if (title.isBlank()) {
                editTitle.error = "Title required"
                return@setOnClickListener
            }
            onAddLektion(title, fromLang, toLang, desc)
            dismiss()
        }

        return AlertDialog.Builder(requireContext())
            .setView(view)
            .create()
    }
}