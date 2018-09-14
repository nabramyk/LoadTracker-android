package com.example.nathan.loadtracker.arrayadapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter

import com.example.nathan.loadtracker.Item

class JobSessionArrayAdapter(context: Context, items: List<Item>) : ArrayAdapter<Item>(context, 0, items) {

    private val mInflater: LayoutInflater = LayoutInflater.from(context)

    enum class RowType {
        LIST_ITEM, HEADER_ITEM
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position)!!.viewType
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getItem(position)!!.getView(mInflater, convertView)
    }
}
