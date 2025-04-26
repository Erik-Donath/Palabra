package de.palabra.palabra

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment

class VocabListFragment : Fragment() {

    private lateinit var vocabList: List<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_vocab_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            vocabList = it.getStringArrayList("list")!!.toList()
        }

        val listView = view.findViewById<ListView>(R.id.vocabListView)
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            vocabList
        )

        listView.adapter = adapter
    }

    companion object {
        fun newInstance(vocabList: List<String>): VocabListFragment {
            return VocabListFragment().apply {
                arguments = bundleOf(
                    "list" to vocabList
                )
            }
        }
    }
}
