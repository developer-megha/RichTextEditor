package com.megha.richtexteditor.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.megha.richtexteditor.R

class ColorGridAdapter(
    private val context: Context,
    private val colorList: List<String>
) : BaseAdapter() {

    override fun getCount(): Int = colorList.size

    override fun getItem(position: Int): Any = colorList[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_color_cell, parent, false)

        val colorView = view.findViewById<View>(R.id.color_view)
        try {
            colorView.background.setTint(Color.parseColor(colorList[position]))
        }
        catch (e: IllegalArgumentException) {
            colorView.background.setTint(Color.BLACK) // fallback
        }

        return view
    }
}
