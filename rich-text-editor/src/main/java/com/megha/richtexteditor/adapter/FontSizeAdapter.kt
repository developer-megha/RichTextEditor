package com.megha.richtexteditor.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.megha.richtexteditor.R

class FontSizeAdapter(
    context: Context,
    private val fontSizesPx: List<Int>
) : ArrayAdapter<Int>(context, R.layout.spinner_dropdown_item, fontSizesPx) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val tv = super.getView(position, convertView, parent) as TextView
        val pxSize = fontSizesPx[position]
        tv.text = "$pxSize px"
        tv.textSize = 18f
        return tv
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val tv = super.getDropDownView(position, convertView, parent) as TextView
        val pxSize = fontSizesPx[position]
        tv.text = "$pxSize px"
        tv.textSize = (pxSize+8).toFloat()
        return tv
    }
}
