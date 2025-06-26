package com.megha.richtexteditor.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.megha.richtexteditor.R

class FontAdapter(
    context: Context,
    private val fonts: List<String>,
    private val fontMap: Map<String, Int>
) : ArrayAdapter<String>(context, R.layout.spinner_dropdown_item, fonts) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val tv = super.getView(position, convertView, parent) as TextView
        val fontResId = fontMap[fonts[position]]
        fontResId?.let {
            val typeface = ResourcesCompat.getFont(context, it)
            tv.typeface = typeface
        }
        return tv
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val tv = super.getDropDownView(position, convertView, parent) as TextView
        val fontResId = fontMap[fonts[position]]
        fontResId?.let {
            val typeface = ResourcesCompat.getFont(context, it)
            tv.typeface = typeface
        }
        return tv
    }
}

