package de.palabra.palabra.ui

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import de.palabra.palabra.R

class LektionActivity : AppCompatActivity() {
    private lateinit var adapter: LektionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lektion)

        findViewById<Button>(R.id.backButton).setOnClickListener { finish() }
        findViewById<Button>(R.id.newButton).setOnClickListener {
            NewLektionDialog(this) { titel, fromLang, toLang ->
                createNewLektion(titel, fromLang, toLang)
                adapter.lektionen = getLektions()
                adapter.notifyDataSetChanged()
        }.show() }

        val recyclerView = findViewById<RecyclerView>(R.id.lektionenRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val lektionen = getLektions()
        adapter = LektionAdapter(lektionen) { startLektion(it) }
        recyclerView.adapter = adapter
    }

    private var lektions: List<Lektion> = listOf(
        Lektion(0, "de", "es", "Unidad 1"),
        Lektion(1, "de", "es", "Unidad 2"),
        Lektion(2, "de", "es", "Unidad 3"),
        Lektion(3, "de", "es", "Unidad 4"),
        )


    private fun getLektions(): List<Lektion> {
        return lektions
    }

    private fun startLektion(lektionID: Long) {
        // Deine Implementierung
    }

    private fun createNewLektion(titel: String, fromLang: String, toLang: String) {
        lektions += Lektion(100, fromLang, toLang, titel)
    }
}

// Datenklasse
data class Lektion(
    val lektionID: Long,
    val fromLang: String,
    val toLang: String,
    val titel: String
)

// Adapter
class LektionAdapter(
    internal var lektionen: List<Lektion>,
    private val onPlayClick: (Long) -> Unit
) : RecyclerView.Adapter<LektionAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.lektionTitle)
        val langs: TextView = view.findViewById(R.id.lektionLangs)
        val playButton: ImageButton = view.findViewById(R.id.playButton)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_lektion, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val lektion = lektionen[position]
        holder.title.text = lektion.titel
        holder.langs.text = "${lektion.fromLang} → ${lektion.toLang}"
        holder.playButton.setOnClickListener {
            onPlayClick(lektion.lektionID)
        }
    }

    override fun getItemCount() = lektionen.size
}

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
