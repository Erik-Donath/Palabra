package de.palabra.palabra.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import de.palabra.palabra.R
import de.palabra.palabra.databinding.ItemLektionBinding
import de.palabra.palabra.databinding.ItemVocabBinding
import de.palabra.palabra.db.Lektion
import de.palabra.palabra.db.LektionWithVocabs
import de.palabra.palabra.db.Vocab

class LektionAdapter(
    private val onLektionDelete: (Lektion) -> Unit,
    private val onVocabDelete: (Vocab) -> Unit,
    private val onLektionExpand: (Int) -> Unit
) : ListAdapter<LektionWithVocabs, LektionAdapter.LektionViewHolder>(DIFF) {

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<LektionWithVocabs>() {
            override fun areItemsTheSame(a: LektionWithVocabs, b: LektionWithVocabs) = a.lektion.id == b.lektion.id
            override fun areContentsTheSame(a: LektionWithVocabs, b: LektionWithVocabs) = a == b
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LektionViewHolder {
        val binding = ItemLektionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LektionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LektionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class LektionViewHolder(private val binding: ItemLektionBinding) : RecyclerView.ViewHolder(binding.root) {
        private var expanded = false
        fun bind(item: LektionWithVocabs) {
            val lektion = item.lektion
            binding.titleText.text = lektion.title
            binding.languagesText.text = "${lektion.fromLangCode} → ${lektion.toLangCode}"
            binding.descriptionText.text = lektion.description

            // TODO: Set flag image based on toLangCode
            binding.flagImage.setImageResource(R.drawable.flag_de)

            // Play button (right)
            binding.playBtn.setOnClickListener {
                // TODO: Play action
            }
            // Add Vocab button
            binding.addVocabBtn.setOnClickListener {
                // TODO: Add vocab dialog
            }
            // Delete Lektion button
            binding.deleteLektionBtn.setOnClickListener {
                onLektionDelete(lektion)
            }

            // Expand/collapse
            binding.lektionCard.setOnClickListener {
                expanded = !expanded
                binding.expansionLayout.isVisible = expanded
                onLektionExpand(lektion.id)
            }

            // Expansion
            binding.expansionLayout.isVisible = expanded
            binding.vocabList.removeAllViews()
            item.vocabs.forEach { vocab ->
                val vocabBinding = ItemVocabBinding.inflate(LayoutInflater.from(binding.vocabList.context), binding.vocabList, false)
                vocabBinding.wordText.text = vocab.word
                vocabBinding.toWordText.text = vocab.toWord
                vocabBinding.deleteVocabBtn.setOnClickListener { onVocabDelete(vocab) }
                binding.vocabList.addView(vocabBinding.root)
            }
        }
    }
}