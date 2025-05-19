package de.palabra.palabra.ui

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import de.palabra.palabra.R

class VocabListAdapter(context: Context, items: List<String>) :
    ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, items) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val tv = super.getView(position, convertView, parent) as TextView
        tv.background = context.getDrawable(R.drawable.bg_rounded)
        tv.setPadding(16, 16, 16, 16)
        return tv
    }
}