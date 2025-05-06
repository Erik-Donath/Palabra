package de.palabra.palabra.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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
        val addVocabButton: Button = view.findViewById(R.id.addVocabButton)
        val deleteLektionButton: Button = view.findViewById(R.id.deleteLektionButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_lektion, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val lektion = lektionen[position]
        val isExpanded = expandedStates[lektion.lektionID] ?: false

        holder.title.text = lektion.titel
        holder.langs.text = "${lektion.fromLang} → ${lektion.toLang}"
        holder.body.visibility = if (isExpanded) View.VISIBLE else View.GONE

        holder.header.setOnClickListener { onExpandToggle(lektion.lektionID) }
        holder.playButton.setOnClickListener { onPlayClick(lektion.lektionID) }

        if (isExpanded) {
            holder.vocabRecycler.layoutManager = LinearLayoutManager(holder.itemView.context)
            holder.vocabRecycler.adapter = VocabAdapter(getVocab(lektion.lektionID) as MutableList<Vocab>)

            holder.addVocabButton.setOnClickListener {
                (holder.itemView.context as? LektionActivity)?.showNewVocabDialog(lektion.lektionID) {
                    holder.vocabRecycler.adapter = VocabAdapter(getVocab(lektion.lektionID) as MutableList<Vocab>)
                }
            }

            holder.deleteLektionButton.setOnClickListener {
                (holder.itemView.context as? LektionActivity)?.deleteLektion(lektion.lektionID)
                lektionen = lektionen.toMutableList().apply { removeAt(position) }
                notifyItemRemoved(position)
            }
        }
    }

    override fun getItemCount() = lektionen.size
}

class VocabAdapter(
    private var vocabs: MutableList<Vocab>
) : RecyclerView.Adapter<VocabAdapter.VocabViewHolder>() {

    inner class VocabViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val fromWord: TextView = view.findViewById(R.id.fromWord)
        val toWord: TextView = view.findViewById(R.id.toWord)
        val deleteButton: ImageButton = view.findViewById(R.id.deleteVocabButton)
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

        holder.deleteButton.setOnClickListener {
            (holder.itemView.context as? LektionActivity)?.deleteVocab(vocabs[position].vocabID)
            vocabs = vocabs.toMutableList().apply { removeAt(position) }
            notifyItemRemoved(position)
        }
    }

    override fun getItemCount() = vocabs.size
}
