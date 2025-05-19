package de.palabra.palabra.ui

import android.content.Intent
import android.os.Bundle
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import de.palabra.palabra.LektionProvider
import de.palabra.palabra.PalabraApplication
import de.palabra.palabra.databinding.ActivityLektionBinding
import de.palabra.palabra.db.Lektion
import de.palabra.palabra.db.Vocab
import kotlinx.coroutines.launch

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
            onLektionExpand = { lektionId -> viewModel.toggleExpansion(lektionId) },
            onAddVocab = { lektionId -> showAddVocabDialog(lektionId) },
            onStartLearn = { lektionId -> startLearnActivity(lektionId)}
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

        binding.homeBtn.setOnClickListener {
            finish()
        }

        binding.addLektionBtn.setOnClickListener {
            showAddLektionDialog()
        }

        viewModel.filteredLektionWithVocabs.observe(this) { lektionen ->
            adapter.submitList(lektionen)
        }
    }

    private fun showAddLektionDialog() {
        AddLektionDialogFragment { title, fromLang, toLang, description ->
            lifecycleScope.launch {
                val repo = (application as PalabraApplication).repository
                repo.insertLektion(
                    Lektion(
                        title = title,
                        fromLangCode = fromLang,
                        toLangCode = toLang,
                        description = description
                    )
                )
            }
        }.show(supportFragmentManager, "AddLektionDialog")
    }

    private fun showAddVocabDialog(lektionId: Int) {
        AddVocabDialogFragment { fromWord, toWord ->
            lifecycleScope.launch {
                val repo = (application as PalabraApplication).repository
                repo.insertVocab(
                    Vocab(
                        lektionId = lektionId,
                        word = fromWord,
                        toWord = toWord
                    )
                )
            }
        }.show(supportFragmentManager, "AddVocabDialog")
    }


    private fun startLearnActivity(lektionId: Int) {
        val repo = (application as PalabraApplication).repository
        lifecycleScope.launch {
            val lektionWithVocab = repo.getLektionWithVocabsSuspend(lektionId)
            if (lektionWithVocab != null && lektionWithVocab.vocabs.isNotEmpty()) {
                val provider = LektionProvider(lektionWithVocab)
                val intent = Intent(this@LektionActivity, LearnActivity::class.java)
                intent.putExtra(LearnActivity.EXTRA_PROVIDER, provider)
                startActivity(intent)
            }
        }
    }
}