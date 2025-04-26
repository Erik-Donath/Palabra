package de.palabra.palabra

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment

class VocabListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_vocab_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Example data - in real app, get from your provider
        val vocabList = listOf(
            "Hund - dog",
            "Katze - cat",
            "Maus - mouse",
            "Haus - house",
            "Auto - car"
        )

        val listView = view.findViewById<ListView>(R.id.vocabListView)
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            vocabList
        )

        listView.adapter = adapter
    }
}
