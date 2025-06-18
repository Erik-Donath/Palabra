package de.palabra.palabra.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import de.palabra.palabra.LektionProviderFunction
import de.palabra.palabra.PalabraApplication
import de.palabra.palabra.R
import de.palabra.palabra.db.Lektion
import de.palabra.palabra.db.Vocab
import de.palabra.palabra.databinding.ActivityLektionBinding
import de.palabra.palabra.VocabProvider
import de.palabra.palabra.util.ExportUtil
import de.palabra.palabra.util.ImportUtil
import de.palabra.palabra.util.applyEdgeToEdgeInsets
import kotlinx.coroutines.launch

class LektionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLektionBinding
    private val viewModel: LektionViewModel by viewModels {
        LektionViewModel.Factory((application as PalabraApplication).repository)
    }
    private lateinit var adapter: LektionAdapter

    private val importLektionLauncher = registerForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let { importUri ->
            lifecycleScope.launch {
                val repo = (application as PalabraApplication).repository
                val success = ImportUtil.importLektionFromUri(this@LektionActivity, importUri, repo)
                if (success) {
                    Toast.makeText(this@LektionActivity, getString(R.string.import_success), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLektionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        applyEdgeToEdgeInsets<View>(R.id.lektion)

        if (intent?.action == Intent.ACTION_VIEW && intent.data != null) {
            val uri = intent.data!!
            lifecycleScope.launch {
                val repo = (application as PalabraApplication).repository
                val success = ImportUtil.importLektionFromUri(this@LektionActivity, uri, repo)
                if (success) {
                    Toast.makeText(this@LektionActivity, getString(R.string.import_success), Toast.LENGTH_SHORT).show()
                }
            }
        }

        adapter = LektionAdapter(
            onLektionDelete = { lektion -> viewModel.deleteLektion(lektion) },
            onVocabDelete = { vocab -> viewModel.deleteVocab(vocab) },
            onLektionExpand = { lektionId -> viewModel.toggleExpansion(lektionId) },
            onAddVocab = { lektionId -> showAddVocabDialog(lektionId) },
            onStartLearn = { lektionId -> startLearnActivity(lektionId)},
            onExportLektion = { lektionId -> exportLektion(lektionId) }
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

        binding.homeBtn.setOnClickListener { finish() }
        binding.addLektionBtn.setOnClickListener { showAddLektionDialog() }
        binding.importLektionBtn.setOnClickListener {
            // Only allow .palabra files
            importLektionLauncher.launch(arrayOf("application/*"))
        }

        viewModel.filteredLektionen.observe(this) { lektionen ->
            adapter.submitLektionen(lektionen)
        }

        viewModel.expandedLektionIds.observe(this) { expandedIds ->
            adapter.updateExpandedIds(expandedIds)
        }

        viewModel.lektionVocabs.observe(this) { vocabsMap ->
            adapter.updateVocabs(vocabsMap)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshData()
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
            viewModel.addVocab(
                Vocab(
                    lektionId = lektionId,
                    word = fromWord,
                    toWord = toWord
                )
            )
        }.show(supportFragmentManager, "AddVocabDialog")
    }

    private fun startLearnActivity(lektionId: Int) {
        lifecycleScope.launch {
            val provider = VocabProvider(LektionProviderFunction(this@LektionActivity, lektionId))

            if (provider.isNotEmpty()) {
                val intent = Intent(this@LektionActivity, LearnActivity::class.java)
                intent.putExtra(LearnActivity.EXTRA_PROVIDER, provider)
                startActivity(intent)
            } else {
                // Handle empty state
                Log.w("Lektion", "There are no vocab's registered to learn.")
                Toast.makeText(this@LektionActivity, getString(R.string.no_vocab_to_learn), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun exportLektion(lektionId: Int) {
        lifecycleScope.launch {
            val repo = (application as PalabraApplication).repository
            val lektionWithVocabs = repo.getLektionWithVocabs(lektionId)
            if (lektionWithVocabs != null) {
                ExportUtil.exportAndShareLektion(this@LektionActivity, lektionWithVocabs)
            }
        }
    }
}
