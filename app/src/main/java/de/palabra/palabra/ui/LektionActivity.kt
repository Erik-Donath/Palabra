package de.palabra.palabra.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import de.palabra.palabra.R

class LektionActivity : AppCompatActivity() {
    private lateinit var viewModel: LektionViewModel
    private lateinit var adapter: LektionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lektion)
        viewModel = ViewModelProvider(this).get(LektionViewModel::class.java)

        setupRecyclerView()
        setupObservers()
        setupClickListeners()
    }

    private fun setupRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.lektionenRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = LektionAdapter(
            lektionen = getLektions(),
            onPlayClick = { startLektion(it) },
            getVocab = { getVocabFromLektionID(it) },
            onExpandToggle = { viewModel.toggleLektionExpansion(it) }
        )
        recyclerView.adapter = adapter
    }

    private fun setupObservers() {
        viewModel.expandedStates.observe(this) { states ->
            adapter.updateExpandedStates(states)
        }
    }

    private fun setupClickListeners() {
        findViewById<View>(R.id.backButton).setOnClickListener { finish() }
        findViewById<View>(R.id.newButton).setOnClickListener { showNewLektionDialog() }
    }

    private fun showNewLektionDialog() {
        NewLektionDialog(this) { titel, fromLang, toLang ->
            createNewLektion(titel, fromLang, toLang)
            adapter.lektionen = getLektions()
            adapter.notifyDataSetChanged()
        }.show()
    }

    // BEHALTE DIE METHODENSIGNATUREN WIE SIE SIND
    private fun getVocabFromLektionID(lektionID: Long): List<Vocab> {
        // DEBUG-IMPLEMENTIERUNG
        return List(5) { index ->
            Vocab(
                vocabID = index.toLong(),
                fromWord = "Wort $index",
                toWord = "Word $index"
            )
        }
    }

    private fun getLektions(): List<Lektion> {
        // DEBUG-IMPLEMENTIERUNG
        return List(3) { index ->
            Lektion(
                lektionID = index.toLong(),
                fromLang = "DE",
                toLang = "EN",
                titel = "Lektion ${index + 1}"
            )
        }
    }

    private fun createNewLektion(titel: String, fromLang: String, toLang: String) {
        // DEINE IMPLEMENTIERUNG
    }

    private fun startLektion(lektionID: Long) {
        // DEINE IMPLEMENTIERUNG
    }
}
