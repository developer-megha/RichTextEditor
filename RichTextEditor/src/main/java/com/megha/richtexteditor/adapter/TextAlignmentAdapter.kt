package com.kitlabs.htmltexteditor.adapter

import android.content.Context
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.megha.richtexteditor.R

class TextAlignmentAdapter(
    context: Context,
    private val images: List<Int>
) : ArrayAdapter<Int>(context, R.layout.spinner_item_with_image, images) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createImageView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createImageView(position, convertView, parent)
    }

    private fun createImageView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.spinner_item_with_image, parent, false)

        val imageView = view.findViewById<ImageView>(R.id.item_image)
        imageView.setImageResource(images[position])

        return view
    }
}
