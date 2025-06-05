package de.palabra.palabra.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import de.palabra.palabra.databinding.ItemLektionBinding
import de.palabra.palabra.databinding.ItemVocabBinding
import de.palabra.palabra.db.Lektion
import de.palabra.palabra.db.Vocab
import de.palabra.palabra.util.LanguageUtil

class LektionAdapter(
    private val onLektionDelete: (Lektion) -> Unit,
    private val onVocabDelete: (Vocab) -> Unit,
    private val onLektionExpand: (Int) -> Unit,
    private val onAddVocab: (Int) -> Unit,
    private val onStartLearn: (Int) -> Unit,
    private val onExportLektion: (Int) -> Unit
) : ListAdapter<Lektion, LektionAdapter.LektionViewHolder>(DIFF) {

    private var expandedIds: Set<Int> = emptySet()
    private var vocabsMap: Map<Int, List<Vocab>> = emptyMap()

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<Lektion>() {
            override fun areItemsTheSame(a: Lektion, b: Lektion) = a.id == b.id
            override fun areContentsTheSame(a: Lektion, b: Lektion) = a == b
        }
    }

    fun submitLektionen(lektionen: List<Lektion>) {
        submitList(lektionen)
    }

    fun updateExpandedIds(expandedIds: Set<Int>) {
        this.expandedIds = expandedIds
        notifyDataSetChanged()
    }

    fun updateVocabs(vocabsMap: Map<Int, List<Vocab>>) {
        this.vocabsMap = vocabsMap
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LektionViewHolder {
        val binding = ItemLektionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LektionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LektionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class LektionViewHolder(private val binding: ItemLektionBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(lektion: Lektion) {
            binding.titleText.text = lektion.title
            binding.languagesText.text = "${lektion.fromLangCode} → ${lektion.toLangCode}"
            binding.descriptionText.text = lektion.description

            binding.flagImage.setImageResource(LanguageUtil.getFlagFromCode(lektion.toLangCode))

            binding.playBtn.setOnClickListener {
                onStartLearn(lektion.id)
            }
            binding.addVocabBtn.setOnClickListener {
                onAddVocab(lektion.id)
            }
            binding.deleteLektionBtn.setOnClickListener {
                onLektionDelete(lektion)
            }
            binding.exportLektionBtn.setOnClickListener {
                onExportLektion(lektion.id)
            }

            val isExpanded = expandedIds.contains(lektion.id)
            binding.expansionLayout.isVisible = isExpanded

            binding.lektionCard.setOnClickListener {
                onLektionExpand(lektion.id)
            }

            if (isExpanded) {
                binding.vocabList.removeAllViews()
                val vocabs = vocabsMap[lektion.id].orEmpty()
                vocabs.forEach { vocab ->
                    val vocabBinding = ItemVocabBinding.inflate(
                        LayoutInflater.from(binding.vocabList.context), 
                        binding.vocabList, 
                        false
                    )
                    vocabBinding.wordText.text = vocab.word
                    vocabBinding.toWordText.text = vocab.toWord
                    vocabBinding.correctCountText.text = "${vocab.correctCount}"
                    vocabBinding.wrongCountText.text = "${vocab.wrongCount}"
                    vocabBinding.deleteVocabBtn.setOnClickListener { onVocabDelete(vocab) }
                    binding.vocabList.addView(vocabBinding.root)
                }
            }
        }
    }
}
