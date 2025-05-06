package de.palabra.palabra.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import de.palabra.palabra.R

data class Lektion(
    val lektionID: Long,
    val fromLang: String,
    val toLang: String,
    val titel: String
)

data class Vocab(
    val vocabID: Long,
    val fromWord: String,
    val toWord: String
)

class LektionAdapter(
    internal var lektionen: List<Lektion>,
    private val onPlayClick: (Long) -> Unit,
    private val getVocab: (Long) -> List<Vocab>,
    private val onExpandToggle: (Long) -> Unit
) : RecyclerView.Adapter<LektionAdapter.ViewHolder>() {

    private var expandedStates: Map<Long, Boolean> = emptyMap()

    fun updateExpandedStates(newStates: Map<Long, Boolean>) {
        expandedStates = newStates
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val header: View = view.findViewById(R.id.headerLayout)
        val title: TextView = view.findViewById(R.id.lektionTitle)
        val langs: TextView = view.findViewById(R.id.lektionLangs)
        val playButton: ImageButton = view.findViewById(R.id.playButton)
        val body: View = view.findViewById(R.id.bodyLayout)
        val vocabRecycler: RecyclerView = view.findViewById(R.id.vocabRecyclerView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_lektion, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val lektion = lektionen[position]
        val isExpanded = expandedStates[lektion.lektionID] ?: false

        with(holder) {
            title.text = lektion.titel
            langs.text = "${lektion.fromLang} → ${lektion.toLang}"
            body.visibility = if (isExpanded) View.VISIBLE else View.GONE

            // Vokabelliste nur bei geöffneter Lektion laden
            if (isExpanded) {
                vocabRecycler.apply {
                    layoutManager = LinearLayoutManager(context)
                    adapter = VocabAdapter(getVocab(lektion.lektionID))
                }
            }

            header.setOnClickListener { onExpandToggle(lektion.lektionID) }
            playButton.setOnClickListener { onPlayClick(lektion.lektionID) }
        }
    }

    override fun getItemCount() = lektionen.size
}

class VocabAdapter(
    private val vocabs: List<Vocab>
) : RecyclerView.Adapter<VocabAdapter.VocabViewHolder>() {

    inner class VocabViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val fromWord: TextView = view.findViewById(R.id.fromWord)
        val toWord: TextView = view.findViewById(R.id.toWord)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VocabViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_vocab, parent, false)
        return VocabViewHolder(view)
    }

    override fun onBindViewHolder(holder: VocabViewHolder, position: Int) {
        val vocab = vocabs[position]
        holder.fromWord.text = vocab.fromWord
        holder.toWord.text = vocab.toWord
    }

    override fun getItemCount() = vocabs.size
}
