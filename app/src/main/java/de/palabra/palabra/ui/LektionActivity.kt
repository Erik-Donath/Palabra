package de.palabra.palabra.ui

import android.os.Bundle
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import de.palabra.palabra.PalabraApplication
import de.palabra.palabra.databinding.ActivityLektionBinding

class LektionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLektionBinding
    private val viewModel: LektionViewModel by viewModels {
        LektionViewModel.Factory((application as PalabraApplication).repository)
    }
    private lateinit var adapter: LektionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLektionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = LektionAdapter(
            onLektionDelete = { lektion -> viewModel.deleteLektion(lektion) },
            onVocabDelete = { vocab -> viewModel.deleteVocab(vocab) },
            onLektionExpand = { lektionId -> viewModel.toggleExpansion(lektionId) }
        )
        binding.lektionRecycler.layoutManager = LinearLayoutManager(this)
        binding.lektionRecycler.adapter = adapter

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.setSearchQuery(query ?: "")
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.setSearchQuery(newText ?: "")
                return true
            }
        })

        // Plus button click (right of SearchView): Not implemented yet
        binding.addLektionBtn.setOnClickListener {
            // TODO: Add Lektion dialog
        }

        viewModel.filteredLektionWithVocabs.observe(this) { lektionen ->
            adapter.submitList(lektionen)
        }
    }
}